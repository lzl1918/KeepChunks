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

public class CommandKeepChunk implements CommandExecutorBase {
    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("current")) {
                if (s instanceof Player) {
                    keepCurrentChunk((Player) s);
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
                    keepChunk(s, x, z, world);
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

    private void keepCurrentChunk(Player player) {
        final Chunk currentChunk = player.getLocation().getChunk();
        final String chunk = currentChunk.getX() + "#" + currentChunk.getZ() + "#" + currentChunk.getWorld().getName();
        if (Utilities.requiredChunks.contains(chunk)) {
            Utilities.msg(player, "&cChunk &f(" + currentChunk.getX() + "," + currentChunk.getZ() + ")&c in world &f'" + currentChunk.getWorld().getName() + "'&c is already marked.");
        } else {
            final String world = currentChunk.getWorld().getName();
            final int x = currentChunk.getX();
            final int z = currentChunk.getZ();
            Utilities.requiredChunks.add(chunk);
            Utilities.data.set("chunks", new ArrayList<>(Utilities.requiredChunks));
            Utilities.saveDataFile();
            Utilities.reloadDataFile();
            Utilities.consoleMsgPrefixed(Strings.DEBUG_PREFIX + "Loading chunk (" + x + "," + z + ") in world '" + world + "'.");
            try {
                Main.instance.getServer().getWorld(world).loadChunk(x, z);
            } catch (NullPointerException ex) {
                Utilities.consoleMsgPrefixed(Strings.DEBUG_PREFIX + "The world '" + world + "' could not be found. Has it been removed?");
            }
            Utilities.msg(player, "&fMarked chunk &9(" + x + "," + z + ")&f in world &6'" + world + "'&f.");
        }
    }

    private void keepChunk(CommandSender s, int x, int z, String world) {
        final String chunk = x + "#" + z + "#" + world;
        if (Utilities.requiredChunks.contains(chunk)) {
            Utilities.msg(s, "&cChunk &f(" + x + "," + z + ")&c in world &f'" + world + "'&c is already marked.");
            return;
        }
        Utilities.requiredChunks.add(chunk);
        Utilities.data.set("chunks", new ArrayList<>(Utilities.requiredChunks));
        Utilities.saveDataFile();
        Utilities.reloadDataFile();
        Utilities.consoleMsgPrefixed(Strings.DEBUG_PREFIX + "Loading chunk (" + x + "," + z + ") in world '" + world + "'.");
        try {
            Main.instance.getServer().getWorld(world).loadChunk(x, z);
        } catch (NullPointerException ex) {
            Utilities.consoleMsgPrefixed(Strings.DEBUG_PREFIX + "The world '" + world + "' could not be found. Has it been removed?");
        }
        Utilities.msg(s, "&fMarked chunk &9(" + x + "," + z + ")&f in world &6'" + world + "'&f.");
    }
}
