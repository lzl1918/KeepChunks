package com.hake.keepchunks.commands;

import com.hake.keepchunks.Strings;
import com.hake.keepchunks.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandReload implements CommandExecutorBase {
    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        if (args.length != 1) {
            Utilities.msg(s, Strings.NOT_SUPPORTED_COMMAND_INVOKE);
            return true;
        }
        Utilities.reloadDataFile();
        Utilities.msg(s, "&aConfiguration files & hooks reloaded successfully.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        return CommandWrapper.filterTabs(tabs, args);
    }
}
