package cn.langchain4j.ai.memory;


import cn.langchain4j.ai.dto.ChatMessageDTO;
import dev.langchain4j.data.message.*;

public class ChatMessageConverter {

    // LangChain4j -> DTO
    public static ChatMessageDTO toDTO(ChatMessage msg) {

        if (msg instanceof UserMessage m) {
            return new ChatMessageDTO("user", m.singleText());
        }

        if (msg instanceof AiMessage m) {
            return new ChatMessageDTO("ai", m.text());
        }

        if (msg instanceof SystemMessage m) {
            return new ChatMessageDTO("system", m.text());
        }

        throw new IllegalArgumentException("Unknown message type: " + msg.getClass());
    }

    // DTO -> LangChain4j
    public static ChatMessage toDomain(ChatMessageDTO dto) {

        return switch (dto.getRole()) {
            case "user" -> UserMessage.from(dto.getContent());
            case "ai" -> AiMessage.from(dto.getContent());
            case "system" -> SystemMessage.from(dto.getContent());
            default -> throw new IllegalArgumentException("Unknown role: " + dto.getRole());
        };
    }
}
