package com.balugaq.rsceditor.api.base;

import com.balugaq.rsceditor.implementation.groups.RSCEItemGroups;
import com.balugaq.rsceditor.implementation.items.tools.MenuCopier;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("deprecation")
public abstract class AbstractContainer extends RSCEAContainer {
    public AbstractContainer(@NotNull SlimefunItemStack item) {
        super(RSCEItemGroups.MACHINE_GROUP, item, RecipeType.NULL, new ItemStack[]{null, null, null, null, null, null, null, null, null});
        setCapacity(1);
        setEnergyConsumption(1);
        setProcessingSpeed(1);
        setBlockMenuPreset();
    }

    @Override
    public void tick(Block b) {

    }

    @Override
    public @Nullable ItemStack getProgressBar() {
        return null;
    }

    @NotNull
    @Override
    public String getMachineIdentifier() {
        return getId();
    }

    @Override
    public int[] getInputSlots() {
        return new int[0];
    }

    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }

    @Override
    public void preRegister() {
        super.preRegister();
        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@NotNull BlockPlaceEvent blockPlaceEvent) {
            }
        });
        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@NotNull BlockBreakEvent blockBreakEvent, @NotNull ItemStack itemStack, @NotNull List<ItemStack> list) {
                Location location = blockBreakEvent.getBlock().getLocation();
                BlockMenu blockMenu = StorageCacheUtils.getMenu(location);
                if (blockMenu != null) {
                    SlimefunItem copierItem = SlimefunItem.getById("RSC_EDITOR_TOOL_MENU_COPIER");
                    if (copierItem instanceof MenuCopier mc) {
                        ItemStack copier = new ItemStack(mc.getItem());
                        MenuCopier.saveMenu0(copier, blockMenu);
                        Player player = blockBreakEvent.getPlayer();
                        World world = player.getWorld();
                        world.dropItemNaturally(location, copier);
                        player.sendMessage(ChatColor.GREEN + "机器菜单已保存到菜单复制器中。");
                    }
                }
            }
        });
    }

    @CanIgnoreReturnValue
    public abstract BlockMenuPreset setBlockMenuPreset();
}
