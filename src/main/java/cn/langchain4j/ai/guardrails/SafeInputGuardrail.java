package cn.langchain4j.ai.guardrails;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.GuardrailResult;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;

import java.util.Set;

/**
 * 输入敏感词检测护栏
 * 拦截包含恶意/敏感词的用户输入
 */
public class SafeInputGuardrail implements InputGuardrail {

    // 敏感词集合
    private static final Set<String> SENSITIVE_WORDS = Set.of(
            "kill", "evil", "suicide", "murder", "terrorist", "bomb"
    );

    /**
     * 核心护栏逻辑：检测输入是否包含敏感词
     */
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {

        // 统一转小写，实现不区分大小写匹配
        String inputText =userMessage.singleText().toLowerCase();
        String[] words = inputText.split("\\W+");
        // 遍历敏感词，检查是否包含
        for (String word : SENSITIVE_WORDS) {
            if (inputText.contains(word.toLowerCase())) {
                // 拦截：返回错误信息，阻止进入AI
                return fatal("检测到敏感内容，请求已被安全拦截"+ word);
            }
        }

        //  通过
        return success();
    }
}