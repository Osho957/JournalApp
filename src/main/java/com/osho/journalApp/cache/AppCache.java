package com.osho.journalApp.cache;

import com.osho.journalApp.entities.ConfigAppEntity;
import com.osho.journalApp.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AppCache {

    private final ConfigRepository configRepository;

    @Autowired
    public AppCache(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public Map<String,String> APP_CACHE;

    @PostConstruct
    public void init() {
        APP_CACHE =  new HashMap<>();
        List<ConfigAppEntity> configs = configRepository.findAll();
        APP_CACHE = configs.stream()
                .collect(Collectors.toMap(ConfigAppEntity::getKey, ConfigAppEntity::getValue));
    }

    @Bean
    public <K,V> RedisTemplate<K,V> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<K,V> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
