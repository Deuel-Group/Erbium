package com.jmsgvn.erbium;

import com.jmsgvn.erbium.utils.Colors;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ConfigValues {
    REDIS_CHANNEL("DATABASE.REDIS.CHANNEL"),
    MONGO_DATABASE("DATABASE.MONGO.DB"),
    MONGO_URI("DATABASE.MONGO.URI"),

    CONSOLE_NAME("GENERAL.CONSOLE_NAME");

    public String path;

    public String format(Erbium plugin) {
        return Colors.convertLegacyColors(plugin.getConfig().getString(path));
    }

    public String noFormat(Erbium plugin) {
        return plugin.getConfig().getString(path);
    }
}
