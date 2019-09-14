package com.hake.keepchunks;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;

public class Events implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChunkUnload(ChunkUnloadEvent e) {
        String worldName = e.getWorld().getName();
        if (getPlayerCountFromWorld(worldName) <= 0 ||
                Utilities.requiredChunks.isEmpty()) {
            return;
        }
        final Chunk currentChunk = e.getChunk();
        final String chunk = currentChunk.getX() + "#" + currentChunk.getZ() + "#"
                + currentChunk.getWorld().getName();
        if (Utilities.requiredChunks.contains(chunk)) {
            try {
                e.setCancelled(true);
                // Main.instance.logInfoFormat("KeepChunk: %s", chunk);
            } catch (NoSuchMethodError ex) {
                Main.instance.getLogger().log(Level.SEVERE, "Could not prevent chunk from being unloaded", ex);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        World world = e.getPlayer().getLocation().getWorld();
        String worldName = world.getName();
        int playerCount = getPlayerCountFromWorld(worldName);
        Main.instance.logInfoFormat("Player joined world %s, # players: %d", worldName, playerCount);
        if (playerCount == 0) {
            loadChunksInWorld(world);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        World fromWorld = e.getFrom();
        int fromWorldPlayerCount = getPlayerCountFromWorld(fromWorld.getName());
        World world = e.getPlayer().getLocation().getWorld();
        String worldName = world.getName();
        int playerCount = getPlayerCountFromWorld(worldName);
        Main.instance.logInfoFormat("Player changed world from %s (%d) to %s (%d)", fromWorld.getName(), fromWorldPlayerCount, worldName, playerCount);

        if (fromWorldPlayerCount <= 0) {
            unloadChunksInWorld(fromWorld);
        }

        if (getPlayerCountFromWorld(worldName) == 1) {
            loadChunksInWorld(world);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent e) {
        World world = e.getPlayer().getLocation().getWorld();
        int playerCount = getPlayerCountFromWorld(world.getName());
        Main.instance.logInfoFormat("Player quited from world %s, remaining players: %d", world.getName(), playerCount);
        if (playerCount <= 1) {
            Main.instance.logInfoFormat("No player in world %s, unload chunks", world.getName());
            unloadChunksInWorld(world);
        }
    }

    private int getPlayerCountFromWorld(String world) {
        try {
            return Main.instance.getServer().getWorld(world).getPlayers().size();
        } catch (Exception ex) {
            return 0;
        }
    }

    private void loadChunksInWorld(World world) {
        String worldName = world.getName();
        final List<String> chunks = new ArrayList<>();
        for (final String chunk : Utilities.requiredChunks) {
            final String chunkWorld = chunk.split("#")[2];
            if (worldName.equalsIgnoreCase(chunkWorld)) {
                chunks.add(chunk);
            }
        }
        if (chunks.isEmpty()) {
            return;
        }

        Main.instance.logInfoFormat("Load %d chunks in world %s: ", chunks.size(), worldName);
        for (String chunk : chunks) {
            final String[] chunkCoordinates = chunk.split("#");
            final int x = Integer.parseInt(chunkCoordinates[0]);
            final int z = Integer.parseInt(chunkCoordinates[1]);
            world.loadChunk(x, z);
        }
    }

    private void unloadChunksInWorld(World world) {
        String worldName = world.getName();
        final List<String> chunks = new ArrayList<>();
        for (final String chunk : Utilities.requiredChunks) {
            final String chunkWorld = chunk.split("#")[2];
            if (worldName.equalsIgnoreCase(chunkWorld)) {
                chunks.add(chunk);
            }
        }
        if (chunks.isEmpty()) {
            return;
        }
        Main.instance.logInfoFormat("Unload %d chunks in world %s: ", chunks.size(), worldName);
        for (String chunk : chunks) {
            final String[] chunkCoordinates = chunk.split("#");
            final int x = Integer.parseInt(chunkCoordinates[0]);
            final int z = Integer.parseInt(chunkCoordinates[1]);
            world.unloadChunk(x, z);
        }
    }
}
