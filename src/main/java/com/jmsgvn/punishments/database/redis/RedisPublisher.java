package com.jmsgvn.punishments.database.redis;

import com.jmsgvn.punishments.ConfigValues;
import com.jmsgvn.punishments.Punishments;
import lombok.Getter;
import redis.clients.jedis.Jedis;

import java.util.LinkedList;
import java.util.Queue;

public class RedisPublisher {

    private @Getter Queue<RedisMessage> messageQueue;
    public RedisPublisher(Jedis jedis, Punishments plugin) {
        this.messageQueue = new LinkedList<>();

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, ()-> {
            if(!messageQueue.isEmpty()) {
                RedisMessage redisMessage = messageQueue.poll();
                jedis.publish(ConfigValues.REDIS_CHANNEL.format(plugin), redisMessage.getMessage().toString());
            }
        }, 1, 1);
    }
}
