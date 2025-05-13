package com.balugaq.rsceditor.implementation.items.machines.container;

import com.balugaq.rsceditor.api.base.AbstractContainer;
import com.balugaq.rsceditor.api.objects.MenuMatrix;
import com.balugaq.rsceditor.utils.YamlWriter;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MenuContainer extends AbstractContainer {
    private static final MenuMatrix matrix = new MenuMatrix()
            .addLine("NNNNNNNNN")
            .addLine("NNNNNNNNN")
            .addLine("NNNNNNNNN")
            .addLine("NNNNNNNNN")
            .addLine("NNNNNNNNN")
            .addLine("NNNNNNNNN");

    public MenuContainer(@NotNull SlimefunItemStack item) {
        super(item);
    }

    @Override
    public @NotNull BlockMenuPreset setBlockMenuPreset() {
        return new BlockMenuPreset(getId(), getItemName()) {
            @Override
            public void init() {
                setSize(54);
                matrix.build(this);
            }

            @Override
            public boolean canOpen(@NotNull Block block, @NotNull Player player) {
                return player.isOp();
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                return new int[0];
            }
        };
    }

    public @NotNull Map<Integer, ItemStack> getMenuContent(@NotNull BlockMenu menu) {
        Map<Integer, ItemStack> content = new HashMap<>();
        for (int i = 0; i < 54; i++) {
            ItemStack item = menu.getItemInSlot(i);
            content.put(i, item);
        }

        return content;
    }

    public @NotNull YamlWriter getAsYamlWriter(@NotNull BlockMenu menu, int[] input_slots, int[] output_slots, @NotNull String id, String title) {
        return getAsYamlWriter(menu, input_slots, output_slots, id, title, 100);
    }

    public @NotNull YamlWriter getAsYamlWriter(@NotNull BlockMenu menu, int[] input_slots, int[] output_slots, @NotNull String id, String title, int progress_bar_slot) {
        if (progress_bar_slot < 0 || progress_bar_slot > 53) {
            progress_bar_slot = 100; // ignore progress bar slot
        }
        YamlWriter writer = new YamlWriter();
        Map<Integer, ItemStack> content = getMenuContent(menu);
        List<Integer> input_slot_list = new ArrayList<>();
        for (int i : input_slots) {
            input_slot_list.add(i);
        }
        List<Integer> output_slot_list = new ArrayList<>();
        for (int i : output_slots) {
            output_slot_list.add(i);
        }

        writer.setRoot(id);
        writer.set("title", title);

        if (progress_bar_slot >= 0 && progress_bar_slot <= 53) {
            ItemStack itemStack = content.get(progress_bar_slot);
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                writer.set("slots." + progress_bar_slot, itemStack.clone());
                writer.set("slots." + progress_bar_slot + ".progressbar", true);
            }
        }

        Set<Integer> keys = new HashSet<>(content.keySet());
        for (int i : keys) {
            if (input_slot_list.contains(i) || output_slot_list.contains(i) || i == progress_bar_slot) {
                content.remove(i);
            }
        }

        for (int i = 0; i < 54; i++) {
            ItemStack item = content.get(i);
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            int last_index = i;
            boolean found = false;
            ItemStack next_item = content.get(i + 1);
            while (next_item != null && SlimefunUtils.isItemSimilar(item, next_item, true, true, true, true)) {
                if (input_slot_list.contains(i) || output_slot_list.contains(i) || i == progress_bar_slot) {
                    found = true;
                    break;
                }
                i++;
                next_item = content.get(i + 1);
                found = true;
            }

            if (found) {
                writer.set("slots." + last_index + "-" + i, item.clone());
            } else {
                writer.set("slots." + last_index, item.clone());
            }
        }

        return writer;
    }
}
