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

public class CommandInfo implements CommandExecutorBase {
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("current")) {
                if (s instanceof Player) {
                    logCurrentChunkInfo((Player) s);
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
                    logChunkInfo(s, x, z, world);
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
                if (newArgs.length == 2) {
                    tabs.add(loc.getWorld().getName());
                }
                if (newArgs.length == 3) {
                    tabs.add(String.valueOf(loc.getChunk().getX()));
                }
                if (newArgs.length == 4) {
                    tabs.add(String.valueOf(loc.getChunk().getZ()));
                }
            } else {
                if (newArgs.length == 2) {
                    tabs.add("<0>");
                }
                if (newArgs.length == 3) {
                    tabs.add("<0>");
                }
                if (newArgs.length == 4) {
                    tabs.add("<world>");
                }
            }
        }
        if (args[1].equals("current")) {
            tabs.clear();
        }
        return CommandWrapper.filterTabs(tabs, args);
    }

    private void logCurrentChunkInfo(Player player) {
        Location location = player.getLocation();
        final Chunk currentChunk = location.getChunk();
        final int x = currentChunk.getX();
        final int z = currentChunk.getZ();
        final int playerX = location.getBlockX();
        final int playerZ = location.getBlockZ();
        final String world = currentChunk.getWorld().getName();
        final String chunk = x + "#" + z + "#" + world;
        Utilities.msg(player, "&2Your current chunk:");
        Utilities.msg(player, "");
        Utilities.msg(player, "&fChunk coords: &6(" + x + ", " + z + ")");
        Utilities.msg(player, "&fCoordinates: &9(" + playerX + ", " + playerZ + ")");
        Utilities.msg(player, "&fWorld: &c" + world);
        if (Utilities.requiredChunks.contains(chunk)) {
            Utilities.msg(player, "&fMarked by KC: &2Yes");
        } else {
            Utilities.msg(player, "&fMarked by KC: &4No");
        }
        try {
            if (Main.instance.getServer().getWorld(world).isChunkLoaded(x, z)) {
                Utilities.msg(player, "&fCurrently loaded: &2Yes");
            } else {
                Utilities.msg(player, "&fCurrently loaded: &4No");
            }
        } catch (NullPointerException e) {
            // doing nothing
        }
    }

    private void logChunkInfo(CommandSender s, int x, int z, String world) {
        try {
            final String chunk = x + "#" + z + "#" + world;
            Utilities.msg(s, "&2The specified chunk:");
            Utilities.msg(s, "");
            Utilities.msg(s, "&fChunk coords: &6(" + x + ", " + z + ")");
            Utilities.msg(s, "&fWorld: &c" + world);
            if (Utilities.requiredChunks.contains(chunk)) {
                Utilities.msg(s, "&fMarked by KC: &2Yes");
            } else {
                Utilities.msg(s, "&fMarked by KC: &4No");
            }
            if (Main.instance.getServer().getWorld(world).isChunkLoaded(x, z)) {
                Utilities.msg(s, "&fCurrently loaded: &2Yes");
            } else {
                Utilities.msg(s, "&fCurrently loaded: &4No");
            }
        } catch (NullPointerException ex) {
            Utilities.msg(s, Strings.INVALID_INPUT);
        }
    }
}
