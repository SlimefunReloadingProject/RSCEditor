package com.balugaq.rsceditor.api;

import com.balugaq.rsceditor.implementation.groups.MyItemGroups;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class ItemGroupItem extends SlimefunItem {
    private final ItemGroup itemGroup;
    public ItemGroupItem(SlimefunItemStack item, ItemGroup itemGroup) {
        super(MyItemGroups.ITEM_GROUP_GROUP, item, RecipeType.NULL, new ItemStack[] {null, null, null, null, null, null, null, null});
        this.itemGroup = itemGroup;
    }
}