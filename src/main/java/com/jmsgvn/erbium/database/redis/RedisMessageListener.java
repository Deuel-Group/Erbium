package com.jmsgvn.erbium.database.redis;

public interface RedisMessageListener {
    void onReceive(RedisMessage redisMessage);
}
