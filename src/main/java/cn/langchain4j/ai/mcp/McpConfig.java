package cn.langchain4j.ai.mcp;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {

    @Value("${bigmodel.api-key}")
    private String apiKey;

    @Bean
    public McpToolProvider mcpToolProvider(){
        // 和 MCP 服务通讯
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("https://open.bigmodel.cn/api/mcp/web_search/sse?Authorization=" + apiKey)
                .logRequests(true)  // 开启请求日志
                .logResponses(true) // 开启响应日志
                .build();
        //创建MCP客户端
        McpClient mcpClient = new DefaultMcpClient.Builder()
                .key("aaaaa")
                .transport(transport)
                .build();

        // 从MCP 中获取工具
        McpToolProvider mcpToolProvider = McpToolProvider.builder()
                .mcpClients(mcpClient)
                .build();
        return mcpToolProvider;
    }
}
