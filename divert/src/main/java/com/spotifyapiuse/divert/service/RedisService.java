package com.spotifyapiuse.divert.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveAuthorizationCode(String key, String authorizationCode) {
        redisTemplate.opsForValue().set(key, authorizationCode, 5, TimeUnit.MINUTES); // 设置过期时间为 5 分钟
    }

    public String getAuthorizationCode(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteAuthorizationCode(String key) {
        redisTemplate.delete(key);
    }
}
