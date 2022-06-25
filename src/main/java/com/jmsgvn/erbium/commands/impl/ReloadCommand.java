package com.jmsgvn.erbium.commands.impl;

import com.jmsgvn.erbium.Locale;
import com.jmsgvn.erbium.Erbium;
import com.jmsgvn.erbium.commands.BaseCommand;
import com.jmsgvn.erbium.utils.Colors;
import com.jmsgvn.erbium.utils.ThreadUtil;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends BaseCommand {

    private final Erbium plugin;

    public ReloadCommand(Erbium plugin, String name) {
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
