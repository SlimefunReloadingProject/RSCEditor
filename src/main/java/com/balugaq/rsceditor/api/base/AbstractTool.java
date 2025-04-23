package com.balugaq.rsceditor.api.base;

import com.balugaq.rsceditor.implementation.groups.RSCEItemGroups;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractTool extends RSCEItem {
    public AbstractTool(@NotNull SlimefunItemStack item) {
        super(RSCEItemGroups.TOOL_GROUP, item, RecipeType.NULL, new ItemStack[]{null, null, null, null, null, null, null, null, null});
    }

    @Override
    public void preRegister() {
        super.preRegister();
        this.addItemHandler(
                (ToolUseHandler) this::toolUse,
                (ItemUseHandler) this::rightClick
        );
    }

    public abstract void toolUse(@NotNull BlockBreakEvent event, @NotNull ItemStack tool, int fortune, @NotNull List<ItemStack> drops);

    public abstract void rightClick(@NotNull PlayerRightClickEvent event);
}
