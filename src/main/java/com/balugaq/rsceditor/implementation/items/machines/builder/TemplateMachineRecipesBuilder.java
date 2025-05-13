package com.balugaq.rsceditor.implementation.items.machines.builder;

import com.balugaq.rsceditor.api.base.AbstractContainer;
import com.balugaq.rsceditor.api.objects.MenuMatrix;
import com.balugaq.rsceditor.utils.ClipboardUtil;
import com.balugaq.rsceditor.utils.Icons;
import com.balugaq.rsceditor.utils.ItemUtil;
import com.balugaq.rsceditor.utils.YamlWriter;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TemplateMachineRecipesBuilder extends AbstractContainer {
    private static final MenuMatrix matrix = new MenuMatrix()
            .addLine("IIIIIIIII")
            .addLine("IIIIIIIII")
            .addLine("IIIIIIIII")
            .addLine("IIIIIIIII")
            .addLine("IIIIIIIBB")
            .addLine("IIIIIIIBG")
            .addItem("G", Icons.build_template_machine_recipe)
            .addItem("B", Icons.white_background);

    public TemplateMachineRecipesBuilder(@NotNull SlimefunItemStack item) {
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
                // Build button
                menu.addMenuClickHandler(matrix.getChar("G"), (p, s, i, a) -> {
                    YamlWriter writer = new YamlWriter();
                    writer.setRoot("recipes");

                    var recipes = ItemUtil.isTemplateMachineRecipes(menu, matrix, "I");
                    if (recipes.getFirstValue()) {
                        for (var recipe : recipes.getSecondValue()) {
                            writer.set("", recipe);
                        }
                    } else {
                        p.sendMessage("§c你还没有放置物品");
                        return false;
                    }

                    ClipboardUtil.send(p, writer.toString());
                    p.sendMessage("§a已编辑完毕");

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
