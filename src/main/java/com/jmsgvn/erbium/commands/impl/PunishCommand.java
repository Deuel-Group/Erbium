package com.jmsgvn.erbium.commands.impl;

import com.jmsgvn.erbium.profiles.Profile;
import com.jmsgvn.erbium.Locale;
import com.jmsgvn.erbium.Erbium;
import com.jmsgvn.erbium.commands.BaseCommand;
import com.jmsgvn.erbium.menus.PunishMenu;
import com.jmsgvn.erbium.utils.ThreadUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PunishCommand extends BaseCommand {
    private final Erbium plugin;

    private String notes = null;

    public PunishCommand(Erbium plugin, String name) {
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
