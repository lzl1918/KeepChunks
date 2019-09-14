package com.hake.keepchunks.commands;

import com.hake.keepchunks.Strings;
import com.hake.keepchunks.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

public class CommandWrapper implements CommandExecutor, TabCompleter {
    private Map<String, CommandExecutorBase> commands;
    private CommandExecutorBase mainExecutor;

    public CommandWrapper() {
        commands = new HashMap<>();
        commands.put("help", new CommandHelp());
        commands.put("reload", new CommandReload());
        commands.put("list", new CommandList());
        commands.put("info", new CommandInfo());
        commands.put("keepchunk", new CommandKeepChunk());
        commands.put("keepregion", new CommandKeepRegion());
        commands.put("releaseall", new CommandReleaseAll());
        commands.put("releasechunk", new CommandReleaseChunk());
        commands.put("releaseregion", new CommandReleaseRegion());
        mainExecutor = new CommandMain();
    }

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        String commandName = command.getName();
        if (!(commandName.equalsIgnoreCase("keepchunks") || commandName.equalsIgnoreCase("kc"))) {
            return true;
        }
        if (args.length == 0) {
            return mainExecutor.onCommand(commandSender, command, label, args);
        }
        String instructionName = args[0];
        for (Map.Entry<String, CommandExecutorBase> entry : commands.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(instructionName)) {
                return entry.getValue().onCommand(commandSender, command, label, args);
            }
        }
        Utilities.msg(commandSender, Strings.GAME_PREFIX + "&cThat command does not exist.");
        return true;
    }

    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }

    public static String[] getArgs(String[] args) {
        ArrayList<String> newArgs = new ArrayList<>();
        for (int i = 0; i < args.length - 1; i++) {
            String s = args[i];
            if (s.trim().isEmpty())
                continue;
            newArgs.add(s);
        }
        return newArgs.toArray(new String[0]);
    }

    public static ArrayList<String> filterTabs(ArrayList<String> list, String[] origArgs) {
        if (origArgs.length == 0)
            return list;
        Iterator<String> iterator = list.iterator();
        String label = origArgs[origArgs.length - 1].toLowerCase();
        while (iterator.hasNext()) {
            String name = iterator.next();
            if (name.toLowerCase().startsWith(label))
                continue;
            iterator.remove();
        }
        return list;
    }
}
