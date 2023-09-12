package com.kokio.productapi.config;

import com.kokio.commonmodule.globalConfig.GlobalAppConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class AppConfig extends GlobalAppConfig {

  //Redis
  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    StringRedisSerializer serializer = new StringRedisSerializer();
    redisTemplate.setConnectionFactory(connectionFactory);
    redisTemplate.setKeySerializer(serializer);
    redisTemplate.setValueSerializer(serializer);
    redisTemplate.setHashKeySerializer(serializer);
    redisTemplate.setHashValueSerializer(serializer);
    return redisTemplate;
  }

}
