package com.hake.keepchunks.commands;

import com.hake.keepchunks.Main;
import com.hake.keepchunks.Strings;
import com.hake.keepchunks.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandReleaseAll implements CommandExecutorBase {
    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        if (args.length != 1) {
            Utilities.msg(s, Strings.NOT_SUPPORTED_COMMAND_INVOKE);
            return true;
        }
        for (final String chunk : Utilities.requiredChunks) {
            final String[] chunkCoordinates = chunk.split("#");
            final int x = Integer.parseInt(chunkCoordinates[0]);
            final int z = Integer.parseInt(chunkCoordinates[1]);
            final String world = chunkCoordinates[2];
        }
        Utilities.requiredChunks.clear();
        Utilities.data.set("chunks", new ArrayList<>());
        Utilities.saveDataFile();
        Utilities.reloadDataFile();
        Utilities.msg(s, "&aAll marked chunks have been released.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        return CommandWrapper.filterTabs(tabs, args);
    }
}
