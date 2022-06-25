package com.jmsgvn.punishments.commands.impl;

import com.jmsgvn.punishments.profiles.Profile;
import com.jmsgvn.punishments.Locale;
import com.jmsgvn.punishments.Punishments;
import com.jmsgvn.punishments.commands.BaseCommand;
import com.jmsgvn.punishments.menus.HistoryMenu;
import com.jmsgvn.punishments.utils.ThreadUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HistoryCommand extends BaseCommand {

    private final Punishments plugin;

    public HistoryCommand(Punishments plugin, String name) {
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
