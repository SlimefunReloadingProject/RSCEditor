package com.balugaq.rsceditor.implementation;


import com.balugaq.rsceditor.core.command.RSCECommands;
import com.balugaq.rsceditor.core.listeners.ItemEditListener;
import com.balugaq.rsceditor.core.managers.ConfigManager;
import com.balugaq.rsceditor.implementation.groups.GroupSetup;
import com.balugaq.rsceditor.implementation.items.AuthorSetup;
import com.balugaq.rsceditor.implementation.items.BiomeItems;
import com.balugaq.rsceditor.implementation.items.ItemGroupItems;
import com.balugaq.rsceditor.implementation.items.MachineSetup;
import com.balugaq.rsceditor.implementation.items.RecipeTypeItems;
import com.balugaq.rsceditor.implementation.items.SoundTypeItems;
import com.balugaq.rsceditor.implementation.items.ToolSetup;
import com.balugaq.rsceditor.implementation.items.TypeItems;
import com.balugaq.rsceditor.utils.SlimefunItemUtil;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import net.guizhanss.guizhanlibplugin.updater.GuizhanUpdater;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Getter
public class RSCEditor extends JavaPlugin implements SlimefunAddon {
    private static RSCEditor instance;
    private String username;
    private String repo;
    private String branch;
    private ConfigManager configManager;

    public static RSCEditor getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.username = "balugaq";
        this.repo = "RSCEditor";
        this.branch = "master";

        GroupSetup.register();

        AuthorSetup.register();
        TypeItems.register();
        MachineSetup.register();
        RecipeTypeItems.register();
        ItemGroupItems.register();
        BiomeItems.register();
        SoundTypeItems.register();
        ToolSetup.register();

        if (getServer().getPluginManager().isPluginEnabled("GuizhanLibPlugin")) {
            getLogger().info("正在尝试自动更新...");
            tryUpdate();
        }

        PluginCommand command = this.getCommand("rsceditor");
        if (command != null) {
            command.setExecutor(new RSCECommands());
        } else {
            getLogger().warning("Failed to register command 'rsceditor'.");
        }

        Bukkit.getPluginManager().registerEvents(new ItemEditListener(), this);

        getLogger().info("RSCEditor has been enabled.");
    }

    public void reload() {
        onDisable();
        onEnable();
    }

    @Override
    public void onDisable() {
        SlimefunItemUtil.unregisterAllItems();
        SlimefunItemUtil.unregisterItemGroups(this);
        getLogger().info("RSCEditor has been disabled.");
    }

    @Override
    @NotNull
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/balugaq/RSCEditor/issues";
    }

    public void tryUpdate() {
        try {
            if (configManager.isAutoUpdate() && getDescription().getVersion().startsWith("Build")) {
                GuizhanUpdater.start(this, getFile(), username, repo, branch);
            }
        } catch (NoClassDefFoundError | NullPointerException | UnsupportedClassVersionError e) {
            getLogger().info("自动更新失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isDebug() {
        return configManager.isDebug();
    }
}