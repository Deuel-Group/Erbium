package com.jmsgvn.erbium.listeners;

import com.jmsgvn.erbium.Locale;
import com.jmsgvn.erbium.Erbium;
import com.jmsgvn.erbium.profiles.Profile;
import com.jmsgvn.erbium.punishments.Punishment;
import com.jmsgvn.erbium.utils.PlayerUtil;
import com.jmsgvn.erbium.utils.ThreadUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

public class PromptListener implements Listener {
    private final Erbium plugin;
    private final Player player;
    private final Punishment punishment;

    public PromptListener(Erbium plugin, Player player, Punishment punishment) {
        this.plugin = plugin;
        this.player = player;
        this.punishment = punishment;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer() != player) {
            return;
        }

        boolean silent = event.getMessage().contains(" -s");

        ThreadUtil.runTask(true, plugin, () -> {
            unpunish(event.getMessage(), silent);
        });

        event.setCancelled(true);
        plugin.unregisterListener(this);
    }

    private void unpunish(String reason, boolean silent) {
        UUID pardoner = player.getUniqueId();

        punishment.setPardoned(new Date());
        punishment.setPardoner(pardoner);
        punishment.setPardonReason(reason);
        punishment.setSilentPardon(silent);
        punishment.execute();
        plugin.getPunishmentManager().push(true, punishment, false);

        Profile targetProfile = PlayerUtil.findPlayer(plugin, punishment.getVictim());
        if (targetProfile == null) {
            plugin.getLogger().log(Level.SEVERE, "Profile not found! Report this issue to developer!");
            return;
        }

        player.sendMessage(Locale.UNPUNISHMENT_SUCCESS.format(plugin)
                .replace("%type%", punishment.getType().pastMessage())
                .replace("%target%", targetProfile.getName())
                .replace("%reason%", reason));
    }
}
