package com.jmsgvn.erbium.utils;

import com.jmsgvn.erbium.Erbium;

public class ThreadUtil {
    public static void runTask(boolean async, Erbium plugin, Runnable runnable) {
        if(async) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
        } else {
            runnable.run();
        }
    }
}
