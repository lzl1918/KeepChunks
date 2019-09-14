package com.hake.keepchunks;

import com.hake.keepchunks.commands.CommandWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;

public class Utilities {
    public static FileConfiguration data;
    private static File dataFile;
    public static HashSet<String> requiredChunks;
    public static HashSet<String> loadedChunks;

    static {
        dataFile = new File(Main.instance.getDataFolder(), "data.yml");
        data = YamlConfiguration.loadConfiguration(dataFile);
        requiredChunks = new HashSet<>(data.getStringList("chunks"));
        loadedChunks = new HashSet<>();
    }

    public static void createConfigs() {
        data.addDefault("chunks", new ArrayList<String>());
        data.options().copyHeader(true);
        data.options().copyDefaults(true);
        saveDataFile();
        reloadDataFile();
    }

    static void registerCommandsAndCompletions() {
        Main.instance.getCommand("keepchunks").setExecutor(new CommandWrapper());
        Main.instance.getCommand("kc").setExecutor(new CommandWrapper());
        Main.instance.getCommand("keepchunks").setTabCompleter(new CommandWrapper());
        Main.instance.getCommand("kc").setTabCompleter(new CommandWrapper());
    }

    public static void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Events(), Main.instance);
    }

    public static void msg(final CommandSender s, final String message) {
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private static void consoleMsg(final String message) {
        Main.instance.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void consoleMsgPrefixed(final String message) {
        Main.instance.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', Strings.INTERNAL_PREFIX + message));
    }

    public static void saveDataFile() {
        if (data == null || dataFile == null) {
            return;
        }
        try {
            data.set("chunks", new ArrayList<>(requiredChunks));
            data.save(dataFile);
        } catch (IOException ex) {
            Main.instance.getLogger().log(Level.SEVERE, "Could not save " + dataFile, ex);
        }
    }

    public static void reloadDataFile() {
        if (dataFile == null) {
            dataFile = new File(Main.instance.getDataFolder(), "data.yml");
        }
        data = YamlConfiguration.loadConfiguration(dataFile);
        requiredChunks = new HashSet<>(data.getStringList("chunks"));
    }
}
