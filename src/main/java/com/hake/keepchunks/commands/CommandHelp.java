package com.hake.keepchunks.commands;

import com.hake.keepchunks.Strings;
import com.hake.keepchunks.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandHelp implements CommandExecutorBase {
    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        if (args.length != 1) {
            Utilities.msg(s, Strings.NOT_SUPPORTED_COMMAND_INVOKE);
            return true;
        }
        Utilities.msg(s, "&8/&akc help  &7-&f  Shows this list");
        Utilities.msg(s, "&8/&akc reload  &7-&f  Reload the plugin");
        Utilities.msg(s, "&8/&akc list  &7-&f  List all marked chunks");
        Utilities.msg(s, "&8/&akc chunkinfo  &7-&f  Info about chunks");
        Utilities.msg(s, "&8/&akc keepchunk  &7-&f  Keep a single chunk loaded");
        Utilities.msg(s, "&8/&akc keepregion  &7-&f  Keep multiple chunks loaded");
        Utilities.msg(s, "&8/&akc releaseall  &7-&f  Release all marked chunks");
        Utilities.msg(s, "&8/&akc releasechunk  &7-&f  Release a single chunk");
        Utilities.msg(s, "&8/&akc releaseregion  &7-&f  Release multiple chunks");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        return CommandWrapper.filterTabs(tabs, args);
    }
}
