package com.jmsgvn.punishments.commands.impl;

import com.jmsgvn.punishments.profiles.Profile;
import com.jmsgvn.punishments.Locale;
import com.jmsgvn.punishments.Punishments;
import com.jmsgvn.punishments.commands.BaseCommand;
import com.jmsgvn.punishments.menus.PunishMenu;
import com.jmsgvn.punishments.utils.ThreadUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PunishCommand extends BaseCommand {
    private final Punishments plugin;

    private String notes = null;

    public PunishCommand(Punishments plugin, String name) {
        super(name);
        this.plugin = plugin;
        this.setAliases("p");
    }

    @Override
    protected void execute(CommandSender sender, String[] args, String alias) {
        if (!sender.hasPermission(Locale.PUNISH_PERMISSION.format(plugin))) {
            sender.sendMessage(Locale.NO_PERMISSION.format(plugin));
            return;
        }

        if (getPlayer(sender) == null) {
            return;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /punish <player> [notes]");
            return;
        }

        ThreadUtil.runTask(true, plugin, () -> {
            if (args.length > 1) {
                StringBuilder sb = new StringBuilder();
                for(int i = 1; i < args.length; i++) {
                    String s = args[i];
                    sb.append(args[i]);
                    if (i + 1 != args.length) {
                        sb.append(" ");
                    }
                }
                notes = sb.toString();
            } else {
                notes = "None";
            }

            Profile targetProfile = getProfile(sender, plugin, args[0]);
            if (targetProfile == null) {
                return;
            }

            PunishMenu.openPunishMenu(plugin, (Player) sender, targetProfile, notes);
        });
    }
}
