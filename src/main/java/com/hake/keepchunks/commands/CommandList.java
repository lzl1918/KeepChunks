package com.hake.keepchunks.commands;

import com.hake.keepchunks.Strings;
import com.hake.keepchunks.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandList implements CommandExecutorBase {
    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        if (args.length != 1) {
            Utilities.msg(s, Strings.NOT_SUPPORTED_COMMAND_INVOKE);
            return true;
        }
        if (Utilities.requiredChunks.isEmpty()) {
            Utilities.msg(s, "&cThere are currently no marked chunks.");
        } else {
            Utilities.msg(s, "&aA list of all marked chunks will be shown below.");
            Utilities.msg(s, "&7---");
            for (final String chunk : Utilities.requiredChunks) {
                final String[] chunkCoordinates = chunk.split("#");
                final int x = Integer.parseInt(chunkCoordinates[0]);
                final int z = Integer.parseInt(chunkCoordinates[1]);
                final String world = chunkCoordinates[2];
                Utilities.msg(s, "&fChunk &9(" + x + "," + z + ") &fin world &6'" + world + "'&f.");
            }
            Utilities.msg(s, "&7---");
            Utilities.msg(s, "&aA total of &f" + Utilities.requiredChunks.size() + "&a chunks are currently marked.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        return CommandWrapper.filterTabs(tabs, args);
    }
}
