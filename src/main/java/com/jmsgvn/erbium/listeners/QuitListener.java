package com.jmsgvn.erbium.listeners;

import com.jmsgvn.erbium.profiles.Profile;
import com.jmsgvn.erbium.Erbium;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private final Erbium plugin;
    public QuitListener(Erbium plugin) {
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
