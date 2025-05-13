package com.balugaq.rsceditor.core.listeners;

import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.Pair;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class ItemEditListener implements Listener {
    public static final Pattern materialPattern = Pattern.compile("m\\((.+)\\)");
    public static final Pattern hungerPattern = Pattern.compile("h\\(([-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?)\\)");
    public static final Pattern machinePattern = Pattern.compile("ma\\((BASIC|AVERAGE|MEDIUM|GOOD|ADVANCED|END_GAME),\\s*(CAPACITOR|GENERATOR|MACHINE)\\)");
    public static final Pattern radioactivePattern = Pattern.compile("ra\\((LOW|MODERATE|HIGH|VERY_HIGH|VERY_DEADLY)\\)");
    public static final Pattern powerBufferPattern = Pattern.compile("pb\\((\\d+)\\)");
    public static final Pattern powerPerSecondPattern = Pattern.compile("pps\\((\\d+)\\)");
    public static final Pattern powerChargedPattern = Pattern.compile("pc\\((\\d+),\\s*(\\d+)\\)");
    public static final Pattern powerPattern = Pattern.compile("p\\((\\d+),\\s*(.+)\\)");
    public static final Pattern rangePattern = Pattern.compile("r\\((\\d+)\\)");
    public static final Pattern usesLeftPattern = Pattern.compile("ul\\((\\d+)\\)");
    public static final Pattern speedPattern = Pattern.compile("s\\(([-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?)\\)");
    private static final Set<PersistentDataType<?, ?>> types = new HashSet<>(Arrays.asList(PersistentDataType.STRING, PersistentDataType.DOUBLE, PersistentDataType.INTEGER, PersistentDataType.BYTE, PersistentDataType.BOOLEAN, PersistentDataType.BYTE_ARRAY, PersistentDataType.FLOAT, PersistentDataType.INTEGER_ARRAY, PersistentDataType.LONG, PersistentDataType.LONG_ARRAY, PersistentDataType.SHORT, PersistentDataType.TAG_CONTAINER, PersistentDataType.TAG_CONTAINER_ARRAY));
    private final Set<Player> editingPlayers = new HashSet<>();
    private final Map<Player, Integer> selectingLines = new HashMap<>();

    public static String compile(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Nonnull
    public static String placeholder(@Nonnull String message) {
        return placeholder(message, null);
    }

    @Nonnull
    public static String placeholder(@Nonnull String message, @Nullable Player player) {
        return placeholder(message, player, "%0");
    }

    @Nonnull
    public static String placeholder(@Nonnull String message, @Nullable Player player, @Nullable String origin) {
        if (message == null || message.isBlank()) {
            return "";
        }
        String newMessage = message
                .replace("%ctu", LoreBuilder.CROUCH_TO_USE)
                .replace("%hsr", LoreBuilder.HAZMAT_SUIT_REQUIRED)
                .replace("%rai", LoreBuilder.RAINBOW)
                .replace("%rcto", LoreBuilder.RIGHT_CLICK_TO_OPEN)
                .replace("%rctu", LoreBuilder.RIGHT_CLICK_TO_USE);
        if (origin != null) {
            newMessage = newMessage.replace("%0", origin);
        }
        if (player != null) {
            newMessage = newMessage.replace("%p", player.getName());
        }
        Matcher matcher = materialPattern.matcher(newMessage);
        newMessage = matcher.replaceAll(matchResult -> {
            String material = matchResult.group(1);

            return LoreBuilder.material(material);
        });
        Matcher matcher2 = hungerPattern.matcher(newMessage);
        newMessage = matcher2.replaceAll(matchResult -> {
            try {
                double hunger = Double.parseDouble(matchResult.group(1));

                return LoreBuilder.hunger(hunger);
            } catch (NumberFormatException e) {
                return "";
            }
        });
        Matcher matcher3 = machinePattern.matcher(newMessage);
        newMessage = matcher3.replaceAll(matchResult -> {
            String tier = matchResult.group(1);
            String type = matchResult.group(2);
            try {
                MachineTier tierEnum = MachineTier.valueOf(tier);
                MachineType typeEnum = MachineType.valueOf(type);
                if (tierEnum == null || typeEnum == null) {
                    return "";
                }

                return LoreBuilder.machine(tierEnum, typeEnum);
            } catch (IllegalArgumentException e) {
                return "";
            }
        });
        Matcher matcher4 = radioactivePattern.matcher(newMessage);
        newMessage = matcher4.replaceAll(matchResult -> {
            String radioactivity = matchResult.group(1);
            try {
                Radioactivity radioactivityEnum = Radioactivity.valueOf(radioactivity);
                if (radioactivityEnum == null) {
                    return "";
                }

                return LoreBuilder.radioactive(radioactivityEnum);
            } catch (IllegalArgumentException e) {
                return "";
            }
        });
        Matcher matcher5 = powerBufferPattern.matcher(newMessage);
        newMessage = matcher5.replaceAll(matchResult -> {
            try {
                int power = Integer.parseInt(matchResult.group(1));

                return LoreBuilder.powerBuffer(power);
            } catch (NumberFormatException e) {
                return "";
            }
        });
        Matcher matcher6 = powerPerSecondPattern.matcher(newMessage);
        newMessage = matcher6.replaceAll(matchResult -> {
            try {
                int power = Integer.parseInt(matchResult.group(1));

                return LoreBuilder.powerPerSecond(power);
            } catch (NumberFormatException e) {
                return "";
            }
        });
        Matcher matcher7 = powerChargedPattern.matcher(newMessage);
        newMessage = matcher7.replaceAll(matchResult -> {
            try {
                int charge = Integer.parseInt(matchResult.group(1));
                int capacity = Integer.parseInt(matchResult.group(2));
                return LoreBuilder.powerCharged(charge, capacity);
            } catch (NumberFormatException e) {
                return "";
            }
        });
        Matcher matcher8 = powerPattern.matcher(newMessage);
        newMessage = matcher8.replaceAll(matchResult -> {
            try {
                int power = Integer.parseInt(matchResult.group(1));
                String suffix = matchResult.group(2);
                return LoreBuilder.power(power, suffix);
            } catch (NumberFormatException e) {
                return "";
            }
        });
        Matcher matcher9 = rangePattern.matcher(newMessage);
        newMessage = matcher9.replaceAll(matchResult -> {
            try {
                int blocks = Integer.parseInt(matchResult.group(1));

                return LoreBuilder.range(blocks);
            } catch (NumberFormatException e) {
                return "";
            }
        });
        Matcher matcher10 = usesLeftPattern.matcher(newMessage);
        newMessage = matcher10.replaceAll(matchResult -> {
            try {
                int usesLeft = Integer.parseInt(matchResult.group(1));

                return LoreBuilder.usesLeft(usesLeft);
            } catch (NumberFormatException e) {
                return "";
            }
        });
        Matcher matcher11 = speedPattern.matcher(newMessage);
        newMessage = matcher11.replaceAll(matchResult -> {
            try {
                float speed = Float.parseFloat(matchResult.group(1));

                return LoreBuilder.speed(speed);
            } catch (NumberFormatException e) {
                return "";
            }
        });

        return newMessage;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEditing(PlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();
        if (!player.isOp()) {
            return;
        }
        if ("rhelp".equals(message)) {
            player.sendMessage(compile("&2=========ItemEditor Help========="));
            player.sendMessage(compile("&brhelp - Show this help message."));
            player.sendMessage(compile("&bdone - Exit the editor."));
            player.sendMessage(compile("&bl - Enter edit mode"));
            player.sendMessage(compile("&2=====ItemEditor Name Commands====="));
            player.sendMessage(compile("&bn [content] - Set the display name of the item to the specified content."));
            player.sendMessage(compile("&bp [content] - Preview the specified content."));
            player.sendMessage(compile("&2=====ItemEditor Lore Commands====="));
            player.sendMessage(compile("&eScroll to select lore lines!"));
            player.sendMessage(compile("&bdd - Delete the selected line."));
            player.sendMessage(compile("&baa [content] - Add a new line at the end of the lore with the specified content."));
            player.sendMessage(compile("&bii [content] - Insert a new line at the selected line with the specified content."));
            player.sendMessage(compile("&bmm [content] - Modify the selected line with the specified content."));
            player.sendMessage(compile("&2=====ItemEditor Rebase Commands====="));
            player.sendMessage(compile("&bcc [slot] - Rebase whole current item to the specified slot."));
            player.sendMessage(compile("&2=====ItemEditor Other Commands====="));
            player.sendMessage(compile("&bt<- - Rebase the current item's type to the next slot."));
            player.sendMessage(compile("&b->t - Rebase the current item's type to the previous slot."));
            player.sendMessage(compile("&btt [hashcode|base64|url|material] - Set the current item's type to the specified type. Allows head textures."));
            player.sendMessage(compile("&b%q[content] - Input content and avoid conflicts with commands."));
            player.sendMessage(compile("&bamt [int] - Set the amount of the current item to the specified value."));
            player.sendMessage(compile("&bcmd [int] - Set the Custom Model Data of the current item"));
            player.sendMessage(compile("&bglow - Glow the item"));
            player.sendMessage(compile("&2==========Placeholders=========="));
            player.sendMessage(compile("&b%ctu = LoreBuilder.CROUCH_TO_USE"));
            player.sendMessage(compile("&b%hsr = LoreBuilder.HAZMAT_SUIT_REQUIRED"));
            player.sendMessage(compile("&b%rai = LoreBuilder.RAINBOW"));
            player.sendMessage(compile("&b%rcto = LoreBuilder.RIGHT_CLICK_TO_OPEN"));
            player.sendMessage(compile("&b%rctu = LoreBuilder.RIGHT_CLICK_TO_USE"));
            player.sendMessage(compile("&b%0 = original lore line"));
            player.sendMessage(compile("&b%p = your name"));
            player.sendMessage(compile("&2======Function Placeholders======"));
            player.sendMessage(compile("&bm(String) = LoreBuilder.material(String)"));
            player.sendMessage(compile("&bh(double) = LoreBuilder.hunger(double)"));
            player.sendMessage(compile("&bs(float) = LoreBuilder.speed(float)"));
            player.sendMessage(compile("&bma(MachineTier, MachineType) = LoreBuilder.machine(MachineTier, MachineType)"));
            player.sendMessage(compile("&bra(Radioactivity) = LoreBuilder.radioactive(Radioactivity)"));
            player.sendMessage(compile("&bpb(int) = LoreBuilder.powerBuffer(int)"));
            player.sendMessage(compile("&bpps(int) = LoreBuilder.powerPerSecond(int)"));
            player.sendMessage(compile("&bpc(int, int) = LoreBuilder.powerCharged(int, int)"));
            player.sendMessage(compile("&bp(int, String) = LoreBuilder.power(int, String)"));
            player.sendMessage(compile("&br(int) = LoreBuilder.range(int)"));
            player.sendMessage(compile("&bul(int) = LoreBuilder.usesLeft(int)"));
            player.sendMessage(compile("&2=========ItemEditor Help========="));
            event.setCancelled(true);
            return;
        } else if ("l".equals(message)) {
            editingPlayers.add(player);
            clearScreen(player);
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType() == Material.AIR) {
                player.sendMessage(compile("&c[RSCEditor] ItemEditor: &cYou need to hold an item in your main hand."));
                return;
            }
            sendLore(player, itemStack.getItemMeta().getLore(), 0);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSelecting(PlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();
        if (!player.isOp()) {
            return;
        }
        if (editingPlayers.contains(player)) {
            if ("done".equals(message)) {
                doneEditing(player);
                event.setCancelled(true);
                return;
            }
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                player.sendMessage(compile("&c[RSCEditor] ItemEditor: &cYou need to hold an item in your main hand."));
                event.setCancelled(true);
                return;
            }
            // commands
            if ("dd".equals(message)) {
                removeLine(player, getSelectingLine(player));
                clearScreen(player);
                sendLore(player, itemStack.getItemMeta().getLore(), getSelectingLine(player));
                event.setCancelled(true);
            } else if (message.startsWith("aa ")) {
                String value = message.substring(3);
                addLine(player, getSelectingLine(player), "&f" + value);
                clearScreen(player);
                sendLore(player, itemStack.getItemMeta().getLore(), getSelectingLine(player));
                event.setCancelled(true);
            } else if (message.startsWith("ii ")) {
                String value = message.substring(3);
                insertLine(player, getSelectingLine(player), "&f" + value);
                clearScreen(player);
                sendLore(player, itemStack.getItemMeta().getLore(), getSelectingLine(player));
                event.setCancelled(true);
            } else if (message.startsWith("mm ")) {
                String value = message.substring(3);
                modifyLine(player, getSelectingLine(player), "&f" + value);
                clearScreen(player);
                sendLore(player, itemStack.getItemMeta().getLore(), getSelectingLine(player));
                event.setCancelled(true);
            } else if (message.startsWith("p ")) {
                String value = message.substring(2);
                player.sendMessage(compile("&c[RSCEditor] ItemEditor: &ePreview: " + compile(value)));
                event.setCancelled(true);
            } else if (message.startsWith("n ")) {
                String value = message.substring(2);
                if (itemStack.getType() == Material.AIR) {
                    player.sendMessage(compile("&c[RSCEditor] ItemEditor: &cYou need to hold an item in your main hand."));
                    event.setCancelled(true);
                    return;
                }
                ItemMeta meta = itemStack.getItemMeta();
                String oldName = meta.getDisplayName();
                meta.setDisplayName(compile(placeholder("&f" + value, player, oldName)));
                itemStack.setItemMeta(meta);
                player.updateInventory();
                event.setCancelled(true);
            } else if ("t<-".equals(message)) {
                int nextSlot;
                int slot = player.getInventory().getHeldItemSlot();
                if (slot >= 8) {
                    nextSlot = 0;
                } else {
                    nextSlot = slot + 1;
                }
                ItemStack anotherItemStack = player.getInventory().getItem(nextSlot);
                if (anotherItemStack == null || anotherItemStack.getType() == Material.AIR) {
                    player.sendMessage(compile("&c[RSCEditor] ItemEditor: &cYou need to contain an item in next slot."));
                    event.setCancelled(true);
                    return;
                }
                itemStack.setType(anotherItemStack.getType());
                setSkullTexture(itemStack, anotherItemStack);
                player.getInventory().setItemInMainHand(anotherItemStack);
                event.setCancelled(true);
            } else if ("->t".equals(message)) {
                int previousSlot;
                int slot = player.getInventory().getHeldItemSlot();
                if (slot <= 0) {
                    previousSlot = 8;
                } else {
                    previousSlot = slot - 1;
                }
                ItemStack anotherItemStack = player.getInventory().getItem(previousSlot);
                if (anotherItemStack == null || anotherItemStack.getType() == Material.AIR) {
                    player.sendMessage(compile("&c[RSCEditor] ItemEditor: &cYou need to contain an item in previous slot."));
                    event.setCancelled(true);
                    return;
                }
                itemStack.setType(anotherItemStack.getType());
                setSkullTexture(itemStack, anotherItemStack);
                player.getInventory().setItemInMainHand(anotherItemStack);
                event.setCancelled(true);
            } else if (message.startsWith("cc ")) {
                String value = message.substring(3);
                try {
                    int slot = Integer.parseInt(value);
                    if (slot < 0 || slot > 8) {
                        player.sendMessage(compile("&c[RSCEditor] ItemEditor: &cInvalid slot number."));
                        event.setCancelled(true);
                        return;
                    }
                    ItemStack anotherItemStack = player.getInventory().getItem(slot);
                    if (anotherItemStack == null || anotherItemStack.getType() == Material.AIR) {
                        player.sendMessage(compile("&c[RSCEditor] ItemEditor: &cYou need to contain an item in the specified slot."));
                        event.setCancelled(true);
                        return;
                    }
                    player.getInventory().setItemInMainHand(itemStack);
                    player.updateInventory();
                    event.setCancelled(true);
                } catch (NumberFormatException e) {
                    player.sendMessage(compile("&c[RSCEditor] ItemEditor: &cInvalid slot number."));
                    event.setCancelled(true);
                    return;
                }
            } else if (message.startsWith("%q")) {
                String value = message.substring(2);
                event.setMessage(value);
                return;
            } else if (message.startsWith("amt ")) {
                String value = message.substring(4);
                try {
                    int amount = Integer.parseInt(value);
                    itemStack.setAmount(amount);
                    player.updateInventory();
                    event.setCancelled(true);
                } catch (NumberFormatException e) {
                    player.sendMessage(compile("&c[RSCEditor] ItemEditor: &cInvalid amount."));
                    event.setCancelled(true);
                    return;
                }
            } else if (message.startsWith("tt ")) {
                String value = message.substring(3).toLowerCase();
                if (isHashcodeLike(value)) {
                    PlayerSkin skin = PlayerSkin.fromHashCode(value);
                    ItemStack head = PlayerHead.getItemStack(skin);
                    setSkullTexture(itemStack, head);
                    player.getInventory().setItemInMainHand(head);
                } else if (isBase64Like(value)) {
                    PlayerSkin skin = PlayerSkin.fromBase64(value);
                    ItemStack head = PlayerHead.getItemStack(skin);
                    setSkullTexture(itemStack, head);
                    player.getInventory().setItemInMainHand(head);
                } else if (isURLLike(value)) {
                    PlayerSkin skin = PlayerSkin.fromURL(value);
                    ItemStack head = PlayerHead.getItemStack(skin);
                    setSkullTexture(itemStack, head);
                    player.getInventory().setItemInMainHand(head);
                } else {
                    Material material = Material.getMaterial(value.toUpperCase());
                    if (material == null || !material.isItem()) {
                        player.sendMessage(compile("&c[RSCEditor] ItemEditor: &cInvalid texture value."));
                        event.setCancelled(true);
                        return;
                    } else {
                        itemStack.setType(material);
                    }
                }
                event.setCancelled(true);
            } else if (message.startsWith("cmd ")) {
                String value = message.substring(4);
                Integer cmd;
                try {
                    cmd = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    cmd = null;
                }

                var meta = itemStack.getItemMeta();
                meta.setCustomModelData(cmd);
                itemStack.setItemMeta(meta);
                player.updateInventory();
                event.setCancelled(true);
            } else if ("glow".equals(message)) {
                var meta = itemStack.getItemMeta();
                meta.addEnchant(Enchantment.LUCK, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemStack.setItemMeta(meta);
                player.updateInventory();
                event.setCancelled(true);
            }

            // intentionally
            else if ("rhelp".equals(message)) {
            } else if ("l".equals(message)) {
            } else {
                player.sendMessage(compile("&c[RSCEditor] ItemEditor: type \"rhelp\" for information."));
                event.setCancelled(true);
            }
        }
    }

    public boolean isHashcodeLike(String value) {
        return value.matches("^[a-fA-F0-9]{32,}$");
    }

    public boolean isBase64Like(String value) {
        return value.length() > 32 && value.matches("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$");
    }

    public boolean isURLLike(String value) {
        return value.matches("^https?://(?:[-\\w]+\\.)?[-\\w]+(?:\\.[a-zA-Z]{2,5}|\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})(?::\\d{1,5})?(/[-\\w./]*)*(\\?[-\\w.&=]*)?(#[-\\w]*)?$");
    }

    public void setSkullTexture(ItemStack itemStack, ItemStack head) {
        head.setAmount(itemStack.getAmount());
        ItemMeta meta = itemStack.getItemMeta();
        ItemMeta headMeta = head.getItemMeta();
        if (meta.hasDisplayName()) {
            headMeta.setDisplayName(meta.getDisplayName());
        }

        if (meta.hasLore()) {
            headMeta.setLore(meta.getLore());
        }
        var pdc = meta.getPersistentDataContainer();

        Map<NamespacedKey, Pair<PersistentDataType<Object, Object>, Object>> data = new HashMap<>();

        for (NamespacedKey key : pdc.getKeys()) {
            for (PersistentDataType<?, ?> atype : types) {
                @SuppressWarnings("unchecked") PersistentDataType<Object, Object> type = (PersistentDataType<Object, Object>) atype;
                try {
                    Object value = pdc.get(key, type);
                    if (value != null) {
                        data.put(key, new Pair<>(type, value));
                    }
                } catch (Exception ignored) {
                }
            }
        }

        data.remove(Slimefun.getItemDataService().getKey());

        for (NamespacedKey key : data.keySet()) {
            headMeta.getPersistentDataContainer().set(key, data.get(key).getFirstValue(), data.get(key).getSecondValue());
        }

        headMeta.addItemFlags(meta.getItemFlags().toArray(new ItemFlag[0]));
        if (meta.hasEnchants()) {
            for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
                headMeta.addEnchant(entry.getKey(), entry.getValue(), true);
            }
        }
        if (meta.hasAttributeModifiers()) {
            headMeta.setAttributeModifiers(meta.getAttributeModifiers());
        }
        if (meta.hasCustomModelData()) {
            headMeta.setCustomModelData(meta.getCustomModelData());
        }
        headMeta.setUnbreakable(meta.isUnbreakable());
        headMeta.setPlaceableKeys(meta.getPlaceableKeys());
        headMeta.setDestroyableKeys(meta.getDestroyableKeys());
        head.setItemMeta(headMeta);
    }

    public void modifyLine(Player player, int line, String value) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        List<String> lore = Objects.requireNonNullElse(itemStack.getItemMeta().getLore(), new ArrayList<>());
        if (line >= 0 && line < lore.size()) {
            String oldLine = lore.get(line);
            lore.set(line, compile(placeholder(value, player, oldLine)));
            ItemMeta meta = itemStack.getItemMeta();
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            player.updateInventory();
        }
    }

    public void addLine(Player player, int line, String value) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        List<String> lore = Objects.requireNonNullElse(itemStack.getItemMeta().getLore(), new ArrayList<>());
        if (line >= 0 && line < lore.size()) {
            String current = lore.get(line);
            lore.add(compile(placeholder(value, player, current)));
        } else {
            lore.add(compile(value));
        }
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        player.updateInventory();
    }

    public void insertLine(Player player, int line, String value) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        List<String> lore = Objects.requireNonNullElse(itemStack.getItemMeta().getLore(), new ArrayList<>());
        if (line >= 0 && line < lore.size()) {
            String current = lore.get(line);
            lore.add(line, compile(placeholder(value, player, current)));
            ItemMeta meta = itemStack.getItemMeta();
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            player.updateInventory();
        }
    }

    @EventHandler
    public void onLineSelect(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (editingPlayers.contains(player)) {
            ItemStack itemStack = player.getInventory().getItem(event.getPreviousSlot());
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                player.sendMessage(compile("&c[RSCEditor] ItemEditor: &cYou need to hold an item in your main hand."));
                return;
            }
            List<String> lore = Objects.requireNonNullElse(itemStack.getItemMeta().getLore(), new ArrayList<>());
            int newSlot = event.getNewSlot();
            int previousSlot = event.getPreviousSlot();
            if (newSlot >= 5 && previousSlot == 0) { // scroll left
                setSelectingLine(player, getSelectingLine(player) - 1, lore.size());
            } else if (newSlot < 5 && previousSlot == 8) { // scroll right
                setSelectingLine(player, getSelectingLine(player) + 1, lore.size());
            } else if (newSlot > previousSlot) { // scroll right
                setSelectingLine(player, getSelectingLine(player) + 1, lore.size());
            } else if (newSlot < previousSlot) { // scroll left
                setSelectingLine(player, getSelectingLine(player) - 1, lore.size());
            }
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                clearScreen(player);
                sendLore(player, lore, getSelectingLine(player));
                event.setCancelled(true);
            }
        }
    }

    public void removeLine(Player player, int line) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        List<String> lore = Objects.requireNonNullElse(itemStack.getItemMeta().getLore(), new ArrayList<>());
        if (line >= 0 && line < lore.size()) {
            lore.remove(line);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            player.updateInventory();
        }
    }

    public int getSelectingLine(Player player) {
        if (!selectingLines.containsKey(player)) {
            selectingLines.put(player, 0);
            return 0;
        }

        return selectingLines.get(player);
    }

    public void setSelectingLine(Player player, int line, int loreSize) {
        if (line < 0) {
            line = 0;
        }
        if (line > loreSize - 1) {
            line = loreSize - 1;
        }
        selectingLines.put(player, line);
    }

    public void clearScreen(Player player) {
        for (int i = 0; i < 20; i++) {
            player.sendMessage("");
        }
    }

    public void sendLore(Player player, List<String> lore, int selectingLine) {
        List<String> showLines = new ArrayList<>();
        List<String> lines;
        if (lore == null) {
            lines = new ArrayList<>();
        } else {
            lines = lore;
        }
        // Show as: " &7|&r    [content]"
        // selecting line show as: "&a>&r    [content]"
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (i == selectingLine) {
                showLines.add(compile(" &a>&r    " + line));
            } else {
                showLines.add(compile(" &7|&r    " + line));
            }
        }
        player.sendMessage(compile("&2=======Edit lore======="));
        for (String line : showLines) {
            player.sendMessage(compile(line));
        }
        player.sendMessage(compile("&2=======Edit lore======="));
    }

    public void doneEditing(Player player) {
        editingPlayers.remove(player);
        selectingLines.remove(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        doneEditing(player);
    }
}
