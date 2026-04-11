package cn.langchain4j.ai;

import cn.langchain4j.ai.rag.RagConfig;
import cn.langchain4j.ai.tools.JavaInfoTool;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
public class AiCodeHelperServiceFactory {

    @Resource
    private ChatModel qwenChatModel;

    @Resource
    private ContentRetriever contentRetriever;

    @Resource
    private McpToolProvider mcpToolProvider;

    @Resource
    private StreamingChatModel streamingChatModel;
//    @Bean
//    public AiCodeHelperService aiCodeHelperService(){
//        return AiServices.create(AiCodeHelperService.class,qwenChatModel);
//    }

    @Bean
    public AiCodeHelperService aiCodeHelperService(){
        // 会话记忆
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        // 构建
        AiCodeHelperService aiCodeHelperService = AiServices.builder(AiCodeHelperService.class)
                .streamingChatModel(streamingChatModel)
                .chatModel(qwenChatModel)
                .chatMemory(chatMemory) // 会话记忆
                .chatMemoryProvider(memoryId->MessageWindowChatMemory.withMaxMessages(10)) //每个会话独立存储
                .contentRetriever(contentRetriever) // 内容检索 (启用 RAG)
                .tools(new JavaInfoTool()) // 工具
                .toolProvider(mcpToolProvider) // mcp
                .build();
        return aiCodeHelperService;
    }

}
