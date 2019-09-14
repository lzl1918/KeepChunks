package com.hake.keepchunks.commands;

import com.hake.keepchunks.Main;
import com.hake.keepchunks.Strings;
import com.hake.keepchunks.Utilities;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class CommandReleaseRegion implements CommandExecutorBase {
    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        if (args.length != 6) {
            Utilities.msg(s, Strings.NOT_SUPPORTED_COMMAND_INVOKE);
            return true;
        }
        try {
            final int x1 = Integer.parseInt(args[1]);
            final int z1 = Integer.parseInt(args[2]);
            final int x2 = Integer.parseInt(args[3]);
            final int z2 = Integer.parseInt(args[4]);
            final String world = args[5];
            final int minX = min(x1, x2);
            final int minZ = min(z1, z2);
            final int maxX = max(x1, x2);
            final int maxZ = max(z1, z2);
            releaseChunkRegions(s, minX, minZ, maxX, maxZ, world);
        } catch (NumberFormatException ex) {
            Utilities.msg(s, Strings.INVALID_INPUT);
        }
        return true;
    }

    private void releaseChunkRegions(CommandSender s, int minX, int minZ, int maxX, int maxZ, String world) {
        Utilities.msg(s, "&fReleasing chunks between &9(" + minX + ", " + minZ + ") (" + maxX + ", " + maxZ + ")&f in world &6'" + world + "'&f...");
        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                final String chunk = x + "#" + z + "#" + world;
                if (!Utilities.requiredChunks.contains(chunk)) {
                    Utilities.msg(s, "&cChunk &f(" + x + "," + z + ")&c in world &f'" + world + "'&c isn't marked.");
                } else {
                    Utilities.requiredChunks.remove(chunk);
                    Utilities.msg(s, "&fReleased chunk &9(" + x + "," + z + ")&f in world &6'" + world + "'&f.");
                }
            }
        }
        Utilities.data.set("chunks", new ArrayList<>(Utilities.requiredChunks));
        Utilities.saveDataFile();
        Utilities.reloadDataFile();
        Utilities.msg(s, "&fReleased chunks between &9(" + minX + ", " + minZ + ") (" + maxX + ", " + maxZ + ")&f in world &6'" + world + "'&f.");
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        String[] newArgs = CommandWrapper.getArgs(args);
        if (s instanceof Player) {
            Player player = (Player) s;
            Location loc = player.getLocation();
            switch (newArgs.length) {
                case 1:
                case 3:
                    tabs.add(String.valueOf(loc.getChunk().getX()));
                    break;
                case 2:
                case 4:
                    tabs.add(String.valueOf(loc.getChunk().getZ()));
                    break;
                case 5:
                    tabs.add(loc.getWorld().getName());
                    break;
            }
            if (newArgs.length > 5) {
                tabs.clear();
            }
            return CommandWrapper.filterTabs(tabs, args);
        }
        return CommandWrapper.filterTabs(tabs, args);
    }
}
