package com.jmsgvn.erbium.commands.impl;

import com.jmsgvn.erbium.profiles.Profile;
import com.jmsgvn.erbium.Locale;
import com.jmsgvn.erbium.Erbium;
import com.jmsgvn.erbium.commands.BaseCommand;
import com.jmsgvn.erbium.menus.HistoryMenu;
import com.jmsgvn.erbium.utils.ThreadUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HistoryCommand extends BaseCommand {

    private final Erbium plugin;

    public HistoryCommand(Erbium plugin, String name) {
        super(name);
        this.plugin = plugin;
        this.setAliases("c", "checkpunishments");
    }

    @Override
    public void execute(CommandSender sender, String[] args, String alias) {
        if (!sender.hasPermission(Locale.HISTORY_PERMISSION.format(plugin))) {
            sender.sendMessage(Locale.NO_PERMISSION.format(plugin));
            return;
        }

        if (getPlayer(sender) == null) {
            return;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /history <player>");
            return;
        }

        ThreadUtil.runTask(true, plugin, () -> {
            Profile targetProfile = getProfile(sender, plugin, args[0]);
            if (targetProfile == null) {
                return;
            }

            HistoryMenu.openHistoryMenu(plugin, (Player) sender, targetProfile, null);
        });
    }
}
