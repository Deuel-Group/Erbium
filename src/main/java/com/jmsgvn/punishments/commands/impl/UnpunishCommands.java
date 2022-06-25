package com.jmsgvn.punishments.commands.impl;

import com.jmsgvn.punishments.profiles.Profile;
import com.jmsgvn.punishments.punishments.Punishment;
import com.jmsgvn.punishments.ConfigValues;
import com.jmsgvn.punishments.Locale;
import com.jmsgvn.punishments.Punishments;
import com.jmsgvn.punishments.commands.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

public class UnpunishCommands extends BaseCommand {

    private final Punishments plugin;

    public UnpunishCommands(Punishments plugin, String name) {
        super(name);
        this.plugin = plugin;
        this.setAliases("unban", "unblacklist", "unmute");
    }

    @Override
    public void execute(CommandSender sender, String[] args, String alias) {
        if (!sender.hasPermission(Locale.UNPUNISH_PERMISSION.format(plugin))) {
            sender.sendMessage(Locale.NO_PERMISSION.format(plugin));
            return;
        }

        String label = alias.toLowerCase();
        if(args.length > 1) {
            Profile targetProfile = getProfile(sender, plugin, args[0]);
            if (targetProfile == null) {
                return;
            }

            Punishment.Type punishmentType;
            switch (label) {
                case "unban":
                    punishmentType = Punishment.Type.BAN;
                    break;
                case "unblacklist":
                    punishmentType = Punishment.Type.BLACKLIST;
                    break;
                case "unmute":
                    punishmentType = Punishment.Type.MUTE;
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Available commands: /unban, /unblacklist, /unmute.");
                    return;
            }

            UUID pardoner = null;
            String pardonerName = ConfigValues.CONSOLE_NAME.format(plugin);
            if(sender instanceof Player) {
                Player player = (Player) sender;
                pardonerName = player.getName();
                pardoner = player.getUniqueId();
            }

            Punishment punishment = targetProfile.getActivePunishment(punishmentType);
            if(punishment != null) {
                StringBuilder sb = new StringBuilder();
                boolean silent = false;
                for(int i = 1; i < args.length; i++) {
                    String s = args[i];
                    if(s.equalsIgnoreCase("-s")) {
                        silent = true;
                    } else {
                        sb.append(args[i]);
                        if (i + 1 != args.length) {
                            sb.append(" ");
                        }
                    }
                }

                punishment.setPardoned(new Date());
                punishment.setPardoner(pardoner);
                punishment.setPardonReason(sb.toString());
                punishment.setSilentPardon(silent);
                punishment.execute();
                plugin.getPunishmentManager().push(true, punishment, false);

                sender.sendMessage(Locale.UNPUNISHMENT_SUCCESS.format(plugin)
                        .replace("%type%", punishmentType.pastMessage())
                        .replace("%target%", targetProfile.getName())
                        .replace("%reason%", sb.toString()));
            } else {
                sender.sendMessage(ChatColor.RED + "The target you specified does not have an active punishment of that type.");
            }

        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <target> <reason> [-s]");
        }
    }
}
