package com.jmsgvn.erbium.database.redis;

import com.jmsgvn.erbium.ConfigValues;
import com.jmsgvn.erbium.Erbium;
import lombok.Getter;
import redis.clients.jedis.Jedis;

import java.util.LinkedList;
import java.util.Queue;

public class RedisPublisher {

    private @Getter Queue<RedisMessage> messageQueue;
    public RedisPublisher(Jedis jedis, Erbium plugin) {
        this.messageQueue = new LinkedList<>();

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, ()-> {
            if(!messageQueue.isEmpty()) {
                RedisMessage redisMessage = messageQueue.poll();
                jedis.publish(ConfigValues.REDIS_CHANNEL.format(plugin), redisMessage.getMessage().toString());
            }
        }, 1, 1);
    }
}
