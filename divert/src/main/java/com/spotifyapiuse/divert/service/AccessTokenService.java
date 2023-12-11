package com.spotifyapiuse.divert.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class AccessTokenService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String getAccessToken(Long userId) {
        // 从 Redis 中获取 Access Token
        String redisKey = "access_token_" + userId;
        return redisTemplate.opsForValue().get(redisKey);
    }

    public void saveAccessToken(String accessToken, Long userId) {
        // 在 Redis 中存储 Access Token，并设置过期时间（以秒为单位，这里设置为一个小时，可以根据需求调整）
        String redisKey = "access_token_" + userId;
        redisTemplate.opsForValue().set(redisKey, accessToken, 1, TimeUnit.HOURS);
    }
}