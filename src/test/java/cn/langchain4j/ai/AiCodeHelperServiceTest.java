
package cn.langchain4j.ai;

import dev.langchain4j.data.message.UserMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeHelperServiceTest {

    @Autowired
    private AiCodeHelperService aiCodeHelperService;
    @Test
    void chatWithMessage() {
        String userMessage = "请给我一个学习java的教程，我现在的本地的 Java 虚拟机和系统的详细信息";
        AiCodeHelperService.Report report = aiCodeHelperService.chatWithMessage(userMessage);
        System.out.println(report);
    }

    @Test
    void chatWithRag() {
        String userMessage = "请给我一个学习java的路线";
        String report = aiCodeHelperService.chatWithRag(userMessage);
        System.out.println(report);
    }

    @Test
    void chatWithMcp() {
        String userMessage = "https://blog.csdn.net/hahai_?spm=1000.2115.3001.10640  这是什么页面";
        String report = aiCodeHelperService.chatWithRag(userMessage);
        System.out.println(report);
    }

    @Test
    void chatWithGuardrails() {
        String userMessage = "kill";
        String report = aiCodeHelperService.chatWithRag(userMessage);
        System.out.println(report);
    }
}