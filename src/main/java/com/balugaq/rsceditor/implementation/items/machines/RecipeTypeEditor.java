package com.balugaq.rsceditor.implementation.items.machines;

import com.balugaq.rsceditor.api.AbstractContainer;
import com.balugaq.rsceditor.api.MenuMatrix;
import com.balugaq.rsceditor.api.TextTypeItem;
import com.balugaq.rsceditor.utils.ClipboardUtil;
import com.balugaq.rsceditor.utils.Icons;
import com.balugaq.rsceditor.utils.ItemUtil;
import com.balugaq.rsceditor.utils.YamlWriter;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.Pair;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @noinspection DataFlowIssue
 */
public class RecipeTypeEditor extends AbstractContainer {
    private static final MenuMatrix matrix = new MenuMatrix()
            .addLine("niBBBBBBB")
            .addLine("NIBBBBBBB")
            .addLine("BBBBBBBBB")
            .addLine("BBBBBBBBB")
            .addLine("BBBBBBBBB")
            .addLine("BBBBBBBBG")
            .addItem("B", ChestMenuUtils.getBackground())
            .addItem("N", Icons.id)
            .addItem("I", Icons.item)
            .addItem("G", Icons.build_recipe_type);

    public RecipeTypeEditor(@NotNull SlimefunItemStack item) {
        super(item);
    }

    @Override
    public @NotNull BlockMenuPreset setBlockMenuPreset() {
        return new BlockMenuPreset(getId(), getItemName()) {
            @Override
            public void init() {
                matrix.build(this);
            }

            @Override
            public void newInstance(@NotNull BlockMenu menu, @NotNull Block b) {
                // Id button
                menu.addMenuClickHandler(matrix.getChar("n"), (p, s, i, a) -> {
                    if (SlimefunItem.getByItem(i) instanceof TextTypeItem typeItem) {
                        p.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                        ChatUtils.awaitInput(p, text -> {
                            typeItem.setContent(i, text);
                            menu.open(p);
                        });
                        return false;
                    }

                    return true;
                });

                // Build button
                menu.addMenuClickHandler(matrix.getChar("G"), (p, s, i, a) -> {
                    YamlWriter writer = new YamlWriter();
                    Pair<Boolean, String> p0 = ItemUtil.isString(menu, matrix, "n");
                    if (!p0.getFirstValue()) {
                        p.sendMessage("你还没有设置这个物品的ID");
                        return false;
                    }
                    String id = p0.getSecondValue();
                    Pair<Boolean, ItemStack> p1 = ItemUtil.isItem(menu, matrix, "i");
                    if (!p1.getFirstValue()) {
                        p.sendMessage("你还没有设置这个物品的物品模型");
                        return false;
                    }
                    ItemStack itemStack = p1.getSecondValue();
                    writer.set(id, itemStack);


                    ClipboardUtil.send(p, writer.toString());

                    return false;
                });
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
}