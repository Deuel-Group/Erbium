package com.jmsgvn.erbium.utils;

import com.jmsgvn.erbium.Erbium;
import com.jmsgvn.erbium.profiles.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerUtil {
    public static Profile findPlayer(Erbium plugin, String target) {
        Player targetPlayer = Bukkit.getPlayer(target);
        Profile targetProfile;

        if (targetPlayer == null) {
            targetProfile = plugin.getProfileManager().find(target, false);
        } else {
            targetProfile = plugin.getProfileManager().get(targetPlayer.getUniqueId());
        }

        return targetProfile;
    }

    public static Profile findPlayer(Erbium plugin, UUID uuid) {
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
