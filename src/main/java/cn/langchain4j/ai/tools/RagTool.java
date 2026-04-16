package cn.langchain4j.ai.tools;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.annotation.Resource;

public class RagTool {

    @Resource
    private EmbeddingStore embeddingStore;
    @Resource
    private EmbeddingModel embeddingModel;
    @Tool("当用户询问Java、Spring、数据库、后端开发等技术问题时，使用该工具查询知识库")
    public String searchKnowledge(String question) {

        // 把问题转成向量
        Embedding queryEmbedding = embeddingModel.embed(question).content();

        // 构建搜索请求
        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(3)
                .minScore(0.7)
                .build();

        // 搜索
        EmbeddingSearchResult<TextSegment> result = embeddingStore.search(request);

        // 拼接结果
        StringBuilder sb = new StringBuilder();
        result.matches().forEach(match -> {
            sb.append(match.embedded().text()).append("\n");
        });

        return sb.toString();
    }
}
