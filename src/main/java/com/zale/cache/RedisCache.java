package com.zale.cache;


import com.zale.utils.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
public class RedisCache implements Cache {

    private String id;

    public RedisCache(String id) {
        log.info("current cache id: [{}]", id);
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        log.info("put key into cache:[{}] put value into cache:[{}]",key,value);
        getRedisTemplate().opsForHash().put(id, key.toString(), value);
    }

    @Override
    public Object getObject(Object key) {
        log.info("get cache key:[{}]",key.toString());
        return getRedisTemplate().opsForHash().get(id,key.toString());
    }

    @Override
    public Object removeObject(Object o) {
        return null;
    }

    @Override
    public void clear() {
        log.info("clear all cache...");
        getRedisTemplate().delete(id);
    }

    @Override
    public int getSize() {
        return 0;
    }

    //encapsulate get redisTemplate
    public RedisTemplate getRedisTemplate() {
         RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
         redisTemplate.setKeySerializer(new StringRedisSerializer());
         redisTemplate.setHashKeySerializer(new StringRedisSerializer());
         return redisTemplate;
    }
}
