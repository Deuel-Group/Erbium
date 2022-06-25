package com.jmsgvn.punishments.utils;

import com.jmsgvn.punishments.profiles.Profile;
import com.jmsgvn.punishments.punishments.Punishment;

public class Stackables {
    public static int offenseNumber(Profile profile, String stack) {
        int offenses = 0;
        for (Punishment punishment : profile.getPunishmentsHistory()) {
            if (punishment.getStack().equals(stack)) {
                offenses++;
            }
        }

        return offenses;
    }
}
