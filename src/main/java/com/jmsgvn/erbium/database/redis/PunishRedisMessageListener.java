package com.jmsgvn.erbium.database.redis;

import com.google.gson.JsonObject;
import com.jmsgvn.erbium.ConfigValues;
import com.jmsgvn.erbium.Locale;
import com.jmsgvn.erbium.Erbium;
import com.jmsgvn.erbium.profiles.Profile;
import com.jmsgvn.erbium.utils.ClickableMessage;
import com.jmsgvn.erbium.utils.Colors;
import org.bukkit.entity.Player;

public class PunishRedisMessageListener implements RedisMessageListener {

    private final Erbium plugin;

    public PunishRedisMessageListener(Erbium plugin) {
        this.plugin = plugin;
        plugin.getRedisSubscriber().getListeners().add(this);
    }

    @Override
    public void onReceive(RedisMessage redisMessage) {
        JsonObject json = redisMessage.getElements();
        if(redisMessage.getInternalChannel().equals(ConfigValues.REDIS_CHANNEL.format(plugin))) {
            RedisAction action = RedisAction.valueOf(json.get("action").getAsString());

            switch(action) {
                case PUNISHMENT:
                    for (Profile profile : plugin.getProfileManager().getProfiles().values()) {
                        Player player = profile.getPlayer();
                        if (player != null && player.isOnline() && player.hasPermission(Locale.SILENT_PERMISSION.format(plugin))) {
                            ClickableMessage message = new ClickableMessage(Colors.convertLegacyColors(json.get("message").getAsString()))
                                    .hover(Colors.convertLegacyColors(json.get("hover").getAsString()));
                            message.sendToPlayer(player);
                        } else if (player != null) {
                            player.sendMessage(Colors.convertLegacyColors(json.get("message").getAsString()));
                        }
                    }
                    break;
                case PUNISHMENT_SILENT:
                    for (Profile profile : plugin.getProfileManager().getProfiles().values()) {
                        Player player = profile.getPlayer();
                        if (player != null && player.isOnline() && player.hasPermission(Locale.SILENT_PERMISSION.format(plugin))) {
                            ClickableMessage message = new ClickableMessage(Colors.convertLegacyColors(json.get("message").getAsString()))
                                    .hover(Colors.convertLegacyColors(json.get("hover").getAsString()));
                            message.sendToPlayer(player);
                        }
                    }
                    break;
            }
        }
    }

    public void close() {
        plugin.getRedisSubscriber().getListeners().remove(this);
    }
}
