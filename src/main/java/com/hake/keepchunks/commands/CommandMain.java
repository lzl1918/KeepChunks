package com.hake.keepchunks.commands;

import com.hake.keepchunks.Strings;
import com.hake.keepchunks.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandMain implements CommandExecutorBase {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        if (Bukkit.getVersion().contains("Spigot")) {
            Utilities.msg(s, Strings.GAME_PREFIX + "&fRunning &9v" + Strings.VERSION + "&f on &cSpigot");
        } else if (Bukkit.getVersion().contains("Paper")) {
            Utilities.msg(s, Strings.GAME_PREFIX + "&fRunning &9v" + Strings.VERSION + "&f on &cPaper");
        } else if (Bukkit.getVersion().contains("Bukkit")) {
            Utilities.msg(s, Strings.GAME_PREFIX + "&fRunning &9v" + Strings.VERSION + "&f on &cBukkit");
        } else {
            Utilities.msg(s, Strings.GAME_PREFIX + "&fRunning &9v" + Strings.VERSION);
        }
        Utilities.msg(s, Strings.GAME_PREFIX + "&fMade by &6Hake");
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<String>();
        tabs.add("help");
        tabs.add("reload");
        tabs.add("list");
        tabs.add("info");
        tabs.add("keepchunk");
        tabs.add("keepregion");
        tabs.add("releaseall");
        tabs.add("releasechunk");
        tabs.add("releaseregion");
        return CommandWrapper.filterTabs(tabs, args);
    }
}
