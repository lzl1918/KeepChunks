package com.hake.keepchunks;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Main extends JavaPlugin {
    public static Main instance;

    public void onEnable() {
        Main.instance = this;
        Utilities.createConfigs();
        Utilities.registerCommandsAndCompletions();
        Utilities.registerEvents();
    }

    public void onDisable() {
        Utilities.reloadDataFile();
        Utilities.saveDataFile();
    }

    public void logInfoFormat(String format, Object ...args) {
        getLogger().log(Level.INFO, Strings.INTERNAL_PREFIX + String.format(format, args));
    }
}
