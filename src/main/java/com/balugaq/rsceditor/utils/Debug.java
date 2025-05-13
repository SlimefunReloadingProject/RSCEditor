package com.balugaq.rsceditor.utils;

import com.balugaq.rsceditor.implementation.RSCEditor;
import org.bukkit.ChatColor;

public class Debug {
    public static void log(String message) {
        if (RSCEditor.getInstance().getConfigManager().isDebug()) {
            RSCEditor.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[RSCEditor] &r" + message));
        }
    }

    public static void log(String... message) {
        for (String msg : message) {
            log(msg);
        }
    }

    public static void log(Object object) {
        log(object == null ? "null" : object.toString());
    }

    public static void log(Object... objects) {
        for (Object object : objects) {
            log(object);
        }
    }
}
