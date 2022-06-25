package com.jmsgvn.punishments.commands.impl;

import com.jmsgvn.punishments.Locale;
import com.jmsgvn.punishments.Punishments;
import com.jmsgvn.punishments.commands.BaseCommand;
import com.jmsgvn.punishments.utils.Colors;
import com.jmsgvn.punishments.utils.ThreadUtil;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends BaseCommand {

    private final Punishments plugin;

    public ReloadCommand(Punishments plugin, String name) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, String alias) {
        if (!sender.hasPermission(Locale.RELOAD_PERMISSION.format(plugin))) {
            sender.sendMessage(Locale.NO_PERMISSION.format(plugin));
            return;
        }

        ThreadUtil.runTask(true, plugin, () -> {
            plugin.reloadMessages();
            sender.sendMessage(Colors.convertLegacyColors("&aReloading messages.yml file!"));
        });
    }
}
