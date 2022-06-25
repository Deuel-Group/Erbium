package com.jmsgvn.erbium.commands.impl;

import com.jmsgvn.erbium.profiles.Profile;
import com.jmsgvn.erbium.Locale;
import com.jmsgvn.erbium.Erbium;
import com.jmsgvn.erbium.commands.BaseCommand;
import com.jmsgvn.erbium.database.mongo.MongoUpdate;
import com.jmsgvn.erbium.evasion.EvasionCheck;
import com.jmsgvn.erbium.utils.ThreadUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class UnexemptCommand extends BaseCommand {

    private final Erbium plugin;

    public UnexemptCommand(Erbium plugin, String name) {
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
            sender.sendMessage(ChatColor.RED + "Usage: /unexempt <player>");
            return;
        }

        ThreadUtil.runTask(true, plugin, () -> {
            Profile targetProfile = getProfile(sender, plugin, args[0]);
            if (targetProfile == null) {
                return;
            }

            UUID uuid = targetProfile.getUuid();

            EvasionCheck evasionCheck = new EvasionCheck(uuid, targetProfile.getCurrentIp(), plugin);
            UUID punishment = evasionCheck.getPunishment();
            boolean exempt = evasionCheck.isExempt();

            if (!exempt) {
                sender.sendMessage("Target is not exempt!");
                return;
            }

            plugin.getMongo().getDocument(false, "punishments", "_id", punishment, document -> {
                if (document != null) {
                    List<UUID> list = document.getList("exemptions", UUID.class) == null ? new ArrayList<>() : document.getList("exemptions", UUID.class);

                    if (list.contains(uuid)) {
                        list.remove(uuid);

                        MongoUpdate mu = new MongoUpdate("punishments", punishment);
                        Map<String, Object> map = new HashMap<>();
                        map.put("exemptions", list);

                        mu.setUpdate(map);
                        plugin.getMongo().massUpdate(false, mu);
                        sender.sendMessage("Removed exemption.");
                    }
                }
            });
        });
    }
}
