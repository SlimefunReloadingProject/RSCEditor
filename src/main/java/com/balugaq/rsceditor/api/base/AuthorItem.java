package com.balugaq.rsceditor.api.base;

import com.balugaq.rsceditor.implementation.groups.RSCEItemGroups;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

public class AuthorItem extends RSCEItem {
    public AuthorItem(SlimefunItemStack item) {
        super(RSCEItemGroups.AUTHOR_GROUP, item, RecipeType.NULL, new ItemStack[]{null, null, null, null, null, null, null, null, null});
    }
}
