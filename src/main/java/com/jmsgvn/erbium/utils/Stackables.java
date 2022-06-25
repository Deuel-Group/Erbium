package com.jmsgvn.erbium.utils;

import com.jmsgvn.erbium.profiles.Profile;
import com.jmsgvn.erbium.punishments.Punishment;

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
