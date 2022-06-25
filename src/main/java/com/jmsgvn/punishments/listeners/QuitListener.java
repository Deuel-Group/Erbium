package com.jmsgvn.punishments.listeners;

import com.jmsgvn.punishments.profiles.Profile;
import com.jmsgvn.punishments.Punishments;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private final Punishments plugin;
    public QuitListener(Punishments plugin) {
        this.plugin = plugin;
        plugin.registerListener(this);
    }

    public void onDisconnect(Player player) {
        Profile profile = plugin.getProfileManager().get(player.getUniqueId());

        if (profile == null) {
            return;
        }

        plugin.getProfileManager().push(true, profile, true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        onDisconnect(event.getPlayer());
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        onDisconnect(event.getPlayer());
    }
}
