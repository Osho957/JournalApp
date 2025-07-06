package com.osho.journalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, String>  redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> T get(String key, Class<T> type) {
        try{
            Object value = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            assert value != null;
            return mapper.readValue(value.toString(), type);
        }catch (Exception e){
            return null;
        }
    }

    public void set(String key, Object value, Long expirationTime) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String valueString = mapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, valueString, expirationTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set value in redis", e);
        }
    }
}
