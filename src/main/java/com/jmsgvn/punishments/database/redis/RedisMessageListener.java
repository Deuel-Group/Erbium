package com.jmsgvn.punishments.database.redis;

public interface RedisMessageListener {
    void onReceive(RedisMessage redisMessage);
}
