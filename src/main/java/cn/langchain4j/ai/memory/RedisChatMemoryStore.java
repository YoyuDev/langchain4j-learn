package cn.langchain4j.ai.memory;

import cn.langchain4j.ai.dto.ChatMessageDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.ChatMessage;

import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;

public class RedisChatMemoryStore implements ChatMemoryStore {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisChatMemoryStore(StringRedisTemplate redisTemplate,
                                ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    private String buildKey(Object memoryId) {
        return "chat_memory:" + memoryId;
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        try {
            String json = redisTemplate.opsForValue().get(buildKey(memoryId));

            if (json == null) {
                return new ArrayList<>();
            }

            List<ChatMessageDTO> dtoList =
                    objectMapper.readValue(json,
                            new TypeReference<List<ChatMessageDTO>>() {});

            return dtoList.stream()
                    .map(ChatMessageConverter::toDomain)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        try {

            List<ChatMessageDTO> dtoList = messages.stream()
                    .map(ChatMessageConverter::toDTO)
                    .toList();

            String json = objectMapper.writeValueAsString(dtoList);

            redisTemplate.opsForValue().set(buildKey(memoryId), json);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteMessages(Object memoryId) {
        redisTemplate.delete(buildKey(memoryId));
    }
}