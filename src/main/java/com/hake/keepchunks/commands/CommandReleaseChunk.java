package com.hake.keepchunks.commands;

import com.hake.keepchunks.Main;
import com.hake.keepchunks.Strings;
import com.hake.keepchunks.Utilities;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandReleaseChunk implements CommandExecutorBase {
    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("current")) {
                if (s instanceof Player) {
                    releaseCurrentChunk((Player) s);
                } else {
                    Utilities.msg(s, Strings.ONLY_PLAYER);
                }
            } else {
                Utilities.msg(s, Strings.NOT_SUPPORTED_COMMAND_INVOKE);
            }
        } else if (args.length == 5) {
            if (args[1].equalsIgnoreCase("coords")) {
                try {
                    final int x = Integer.parseInt(args[2]);
                    final int z = Integer.parseInt(args[3]);
                    final String world = args[4];
                    releaseChunk(s, x, z, world);
                } catch (NumberFormatException ex) {
                    Utilities.msg(s, Strings.INVALID_INPUT);
                }
            } else {
                Utilities.msg(s, Strings.NOT_SUPPORTED_COMMAND_INVOKE);
            }
        } else {
            Utilities.msg(s, Strings.NOT_SUPPORTED_COMMAND_INVOKE);
        }
        return true;
    }

    private void releaseChunk(CommandSender s, int x, int z, String world) {
        final String chunk = x + "#" + z + "#" + world;
        if (!Utilities.requiredChunks.contains(chunk)) {
            Utilities.msg(s, "&cChunk &f(" + x + "," + z + ")&c in world &f'" + world + "'&c isn't marked.");
        } else {
            Utilities.requiredChunks.remove(chunk);
            Utilities.data.set("chunks", new ArrayList<>(Utilities.requiredChunks));
            Utilities.saveDataFile();
            Utilities.reloadDataFile();
            Utilities.msg(s, "&fReleased chunk &9(" + x + "," + z + ")&f in world &6'" + world + "'&f.");
        }
    }

    private void releaseCurrentChunk(Player s) {
        final Chunk currentChunk = s.getLocation().getChunk();
        final String chunk = currentChunk.getX() + "#" + currentChunk.getZ() + "#" + currentChunk.getWorld().getName();
        if (!Utilities.requiredChunks.contains(chunk)) {
            Utilities.msg(s, "&cChunk &f(" + currentChunk.getX() + "," + currentChunk.getZ() + ")&c in world &f'" + currentChunk.getWorld().getName() + "'&c isn't marked.");
        } else {
            final String world = currentChunk.getWorld().getName();
            final int x = currentChunk.getX();
            final int z = currentChunk.getZ();
            Utilities.requiredChunks.remove(chunk);
            Utilities.data.set("chunks", new ArrayList<>(Utilities.requiredChunks));
            Utilities.saveDataFile();
            Utilities.reloadDataFile();
            Utilities.msg(s, "&fReleased chunk &9(" + x + "," + z + ")&f in world &6'" + world + "'&f.");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        String[] newArgs = CommandWrapper.getArgs(args);
        if (newArgs.length == 1) {
            tabs.add("current");
            tabs.add("coords");
        } else if (args[1].equals("coords")) {
            if (s instanceof Player) {
                Player player = (Player) s;
                Location loc = player.getLocation();
                switch (newArgs.length) {
                    case 2:
                        tabs.add(String.valueOf(loc.getChunk().getX()));
                        break;
                    case 3:
                        tabs.add(String.valueOf(loc.getChunk().getZ()));
                        break;
                    case 4:
                        tabs.add(loc.getWorld().getName());
                        break;
                }
            } else {
                switch (newArgs.length) {
                    case 2:
                    case 3:
                        tabs.add("<0>");
                        break;
                    case 4:
                        tabs.add("<world>");
                        break;
                }
            }
        }
        if (args[1].equals("current")) {
            tabs.clear();
        }
        return CommandWrapper.filterTabs(tabs, args);
    }
}
