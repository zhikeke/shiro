package com.shiro.service.impl;

import com.google.common.base.Joiner;
import com.shiro.beans.CacheKeyConstants;
import com.shiro.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/11 15:41
 */
@Service
@Slf4j
public class SysCacheService {
    @Resource(name = "redisPool")
    private RedisPool redisPool;

    public void saveCache(String toSaveValue, int timeutSeconds, CacheKeyConstants prefix) {
         saveCache(toSaveValue, timeutSeconds, prefix, null);
    }

    public void saveCache(String toSaveValue, int timeutSeconds, CacheKeyConstants prefix, String ... keys) {
        if (toSaveValue == null) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            String cacheKey = generateCacheKey(prefix, keys);
            shardedJedis = redisPool.instance();
            shardedJedis.setex(cacheKey, timeutSeconds, toSaveValue);
        } catch (Exception e) {
            log.error("save cache exception, prefix:{}, keys:{}, ex:{}", prefix.name(), JsonMapper.obj2String(keys), e);
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    public String selectFromCache(CacheKeyConstants prefix, String ... keys) {
        ShardedJedis shardedJedis = null;
        String cacheKey = generateCacheKey(prefix, keys);
        try {
            shardedJedis = redisPool.instance();
            return shardedJedis.get(cacheKey);
        } catch (Exception e) {
            log.error("get cache exception, prefix:{}, keys:{}, ex:{}", prefix.name(), JsonMapper.obj2String(keys), e);
            return null;
        } finally {
            redisPool.safeClose(shardedJedis);
        }

    }

    public  String generateCacheKey(CacheKeyConstants prefix, String ... keys ) {
          String key = prefix.name();
          if (keys != null && keys.length > 0) {
              key += "_" + Joiner.on("_").join(keys);
          }
          return key;
    }



}
