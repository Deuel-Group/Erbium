package com.jmsgvn.punishments.commands.impl;

import com.jmsgvn.punishments.profiles.Profile;
import com.jmsgvn.punishments.Locale;
import com.jmsgvn.punishments.Punishments;
import com.jmsgvn.punishments.commands.BaseCommand;
import com.jmsgvn.punishments.evasion.EvasionCheck;
import com.jmsgvn.punishments.utils.ThreadUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class WhyBannedCommand extends BaseCommand {

    private final Punishments plugin;

    public WhyBannedCommand(Punishments plugin, String name) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, String alias) {
        if (!sender.hasPermission(Locale.HISTORY_PERMISSION.format(plugin))) {
            sender.sendMessage(Locale.NO_PERMISSION.format(plugin));
            return;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /whybanned <player>");
            return;
        }

        ThreadUtil.runTask(true, plugin, () -> {
            Profile targetProfile = getProfile(sender, plugin, args[0]);
            if (targetProfile == null) {
                return;
            }

            EvasionCheck evasionCheck = new EvasionCheck(targetProfile.getUuid(), targetProfile.getCurrentIp(), plugin);
            String message = evasionCheck.getWhy();

            if (message == null) {
                sender.sendMessage("Target should be able to join!");
            } else {
                sender.sendMessage(message);
            }
        });
    }
}
