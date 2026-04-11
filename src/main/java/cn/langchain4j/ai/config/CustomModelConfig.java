package cn.langchain4j.ai.config;

import com.arize.instrumentation.langchain4j.LangChain4jInstrumentor;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;

import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CustomModelConfig {

    @Value("${dashscope.api-key}")
    private String apiKey;

    @Value("${dashscope.model-name}")
    private String modelName;

    // 全局 tracerProvider
    private final SdkTracerProvider tracerProvider;
    // 全局 instrumentor
    private final LangChain4jInstrumentor instrumentor;

    public CustomModelConfig() {
        // OTLP exporter
        OtlpGrpcSpanExporter exporter = OtlpGrpcSpanExporter.builder()
                .setEndpoint("http://localhost:4317")
                .build();

        // tracerProvider
        this.tracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(exporter).build())
                .build();

        // instrument 调用
        this.instrumentor = LangChain4jInstrumentor.instrument(tracerProvider);
    }

    @Bean
    public ChatModel chatModel() {
        return QwenChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .listeners(List.of(instrumentor.createModelListener()))
                .build();
    }

    @Bean
    public StreamingChatModel streamingChatModel() {
        return QwenStreamingChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .listeners(List.of(instrumentor.createModelListener()))
                .build();
    }
}