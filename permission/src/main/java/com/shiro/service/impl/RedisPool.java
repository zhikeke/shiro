package com.shiro.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/11 15:37
 */
@Service("redisPool")
@Slf4j
public class RedisPool {
    @Resource(name = "shardedJedisPool")
    private ShardedJedisPool shardedJedisPool;


    public ShardedJedis instance() {
        return shardedJedisPool.getResource();
    }

    public void safeClose(ShardedJedis shardedJedis) {
        if (shardedJedis == null) {
            return;
        }
        try {
            shardedJedis.close();
        } catch (Exception e) {
            log.error("close redis resource exception", e);
        }

    }


}
