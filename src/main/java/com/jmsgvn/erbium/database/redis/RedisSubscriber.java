package com.jmsgvn.erbium.database.redis;

import com.jmsgvn.erbium.ConfigValues;
import com.jmsgvn.erbium.Erbium;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.HashSet;
import java.util.Set;

public class RedisSubscriber {

    private final JedisPubSub jedisPubSub;
    private @Getter Set<RedisMessageListener> listeners;
    private final String rChannel;
    public RedisSubscriber(Jedis jedis, Erbium plugin) {
        this.listeners = new HashSet<>();

        this.rChannel = ConfigValues.REDIS_CHANNEL.format(plugin);

        this.jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if(rChannel.equals(channel)) {
                    for (RedisMessageListener listener : listeners) {
                        listener.onReceive(new RedisMessage(message));
                    }
                }
            }
        };

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, ()-> {
           jedis.subscribe(jedisPubSub, rChannel);
        });
    }
}
