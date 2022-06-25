package com.jmsgvn.punishments.profiles;

import com.jmsgvn.punishments.Punishments;
import com.jmsgvn.punishments.punishments.Punishment;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public @Data class Profile {

    private final Punishments plugin;
    private final UUID uuid;
    private String name;
    private String currentIp;
    private List<String> ipHistory;
    private List<UUID> punishments;

    public Profile(Punishments plugin, UUID uuid) {
        this.plugin = plugin;
        this.name = "null";
        this.uuid = uuid;
        this.ipHistory = new ArrayList<>();
        this.punishments = new ArrayList<>();
    }

    public Profile(Punishments plugin, Player player) {
        this(plugin, player.getUniqueId());
        this.name = player.getName();
        this.currentIp = player.getAddress().getAddress().getHostAddress();
    }

    public Punishment getActivePunishment(Punishment.Type type) {
        for(Punishment punishment : getPunishments(type)) {
            if (punishment.isActive()) {
                return punishment;
            }
        }
        return null;
    }

    public List<Punishment> getPunishments(Punishment.Type type) {
        List<Punishment> punishments = new ArrayList<>();
        for(UUID uuid : this.punishments) {
            Punishment punishment = plugin.getPunishmentManager().getPunishment(uuid);
            if(punishment != null && punishment.getType().equals(type)) {
                punishments.add(punishment);
            }
        }
        return punishments;
    }

    public List<Punishment> getPunishmentsHistory() {
        List<Punishment> punishments = new ArrayList<>();
        for(UUID uuid : this.punishments) {
            Punishment punishment = plugin.getPunishmentManager().getPunishment(uuid);
            if(punishment != null) {
                punishments.add(punishment);
            }
        }
        return punishments;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void addIp(String ip) {
        this.currentIp = ip;
        if(!ipHistory.contains(ip)) {
            ipHistory.add(ip);
        }
    }

    public Punishment punish(Punishment.Type type, String stack, UUID issuer, String reason, Date expires, boolean silent) {
        Punishment punishment = plugin.getPunishmentManager().create(type, stack, this, issuer, reason, expires, silent);
        if(punishment != null) {
            punishment.execute();
        }
        return punishment;
    }

    public void update() {
        punishments.removeIf(uuid -> plugin.getPunishmentManager().getPunishment(uuid) == null);
    }
}
