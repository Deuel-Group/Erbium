package com.jmsgvn.punishments.punishments;

import com.jmsgvn.punishments.profiles.Profile;
import com.jmsgvn.punishments.Punishments;
import lombok.Getter;

import java.util.*;

public class PunishmentManager {

    private final Punishments plugin;
    @Getter private Map<UUID, Punishment> punishments;
    public PunishmentManager(Punishments plugin) {
        this.plugin = plugin;
        this.punishments = new HashMap<>();
    }

    public Punishment create(Punishment.Type type, String stack, Profile victim, UUID issuer, String reason, Date expires, boolean silent) {
        for(Punishment punishment : victim.getPunishments(type)) {
            if(punishment.isActive() && !type.equals(Punishment.Type.WARN) && !type.equals(Punishment.Type.KICK)) {
                return null;
            }
        }

        Punishment punishment = new Punishment(plugin, UUID.randomUUID());
        punishment.setType(type);
        punishment.setStack(stack);
        punishment.setVictim(victim.getUuid());
        punishment.setIssuer(issuer);
        punishment.setIssueReason(reason);
        punishment.setIssued(new Date());
        punishment.setExpires(expires);
        punishment.setSilentIssue(silent);

        punishments.put(punishment.getUuid(), punishment);
        victim.getPunishments().add(punishment.getUuid());

        push(true, punishment, false);

        if(victim.getPlayer() == null) {
            plugin.getProfileManager().push(true, victim, false);
        }

        return punishment;
    }

    public Punishment getPunishment(UUID uuid) {
        return punishments.get(uuid);
    }

    public void push(boolean async, Punishment punishment, boolean unload) {
        plugin.getStorage().savePunishment(async, punishment);

        if(unload) {
            punishments.remove(punishment.getUuid());
        }
    }
}
