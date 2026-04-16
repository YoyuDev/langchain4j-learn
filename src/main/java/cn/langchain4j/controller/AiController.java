package cn.langchain4j.controller;


import cn.langchain4j.ai.AiCodeHelperService;
import jakarta.annotation.Resource;
import org.apache.catalina.Server;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class AiController {

    @Resource
    private AiCodeHelperService aiCodeHelperService;


//    @GetMapping("/chat")
//    public Flux<ServerSentEvent<String>> chat(int memoryId, String message) {
//        return aiCodeHelperService.chatStream(memoryId, message)
//                .map(chunk -> ServerSentEvent.<String>builder()
//                        .data(chunk)
//                        .build());
//
//    }

    @GetMapping(value = "/chat", produces = "text/event-stream")
    public Flux<ServerSentEvent<String>> chat(int memoryId, String message) {
        if (message == null || message.isBlank()) {
            // 返回空的 Flux，拦截请求
            return Flux.empty();
        }

        // 正常流程
        return aiCodeHelperService.chatStream(memoryId, message)
                .bufferUntil(chunk ->
                        chunk.endsWith("。") ||
                                chunk.endsWith("！") ||
                                chunk.endsWith("？") ||
                                chunk.endsWith("\n")
                )
                .map(list -> String.join("", list))
                .map(sentence -> ServerSentEvent.<String>builder()
                        .data(sentence)
                        .build());
    }
}
