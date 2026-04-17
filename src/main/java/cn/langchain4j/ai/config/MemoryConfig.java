package cn.langchain4j.ai.config;

import cn.langchain4j.ai.memory.RedisChatMemoryStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class MemoryConfig {


    // redis
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }

    @Bean
    public RedisChatMemoryStore redisChatMemoryStore(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper) {

        return new RedisChatMemoryStore(redisTemplate, objectMapper);
    }


}