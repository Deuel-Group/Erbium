package com.jmsgvn.punishments.utils;

import com.jmsgvn.punishments.Punishments;

public class ThreadUtil {
    public static void runTask(boolean async, Punishments plugin, Runnable runnable) {
        if(async) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
        } else {
            runnable.run();
        }
    }
}
