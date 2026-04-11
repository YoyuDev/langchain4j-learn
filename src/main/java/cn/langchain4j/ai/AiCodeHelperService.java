package cn.langchain4j.ai;

import cn.langchain4j.ai.guardrails.SafeInputGuardrail;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;
import reactor.core.publisher.Flux;

import java.util.List;
// 护轨
@InputGuardrails({SafeInputGuardrail.class})
public interface AiCodeHelperService {
    @SystemMessage(fromResource = "system-prompt.txt")
    String chat(String userMessage);

    @SystemMessage(fromResource = "system-prompt.txt")
    Report chatWithMessage(String userMessage);

    @SystemMessage(fromResource = "system-prompt.txt")

    Flux<String> chatStream(
            @MemoryId int memoryId,
            @UserMessage String message
    );

    //学习报告
    record Report(String name, List<String> suggestionList) {}

    @SystemMessage(fromResource = "system-prompt-rag.txt")
    String chatWithRag(String userMessage);
}
