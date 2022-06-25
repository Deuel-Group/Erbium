package com.jmsgvn.punishments.utils;

import com.jmsgvn.punishments.Punishments;
import com.jmsgvn.punishments.profiles.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerUtil {
    public static Profile findPlayer(Punishments plugin, String target) {
        Player targetPlayer = Bukkit.getPlayer(target);
        Profile targetProfile;

        if (targetPlayer == null) {
            targetProfile = plugin.getProfileManager().find(target, false);
        } else {
            targetProfile = plugin.getProfileManager().get(targetPlayer.getUniqueId());
        }

        return targetProfile;
    }

    public static Profile findPlayer(Punishments plugin, UUID uuid) {
        Player targetPlayer = Bukkit.getPlayer(uuid);
        Profile targetProfile;

        if (targetPlayer == null) {
            targetProfile = plugin.getProfileManager().find(uuid, false);
        } else {
            targetProfile = plugin.getProfileManager().get(targetPlayer.getUniqueId());
        }

        return targetProfile;
    }
}
