package cn.langchain4j.ai.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RagDataInitializer implements CommandLineRunner {

    @Resource
    private EmbeddingModel embeddingModel;

    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;

    @Override
    public void run(String... args) {

        System.out.println("开始初始化 RAG 数据...");

        try {
            //  强制清空 Qdrant（避免历史脏数据）
            if (embeddingStore instanceof QdrantEmbeddingStore qdrant) {
                try {
                    qdrant.clearStore();
                    System.out.println("已清空 Qdrant 集合");
                } catch (Exception e) {
                    System.out.println("集合不存在，将自动创建");
                }
            }

            //  加载文档
            List<Document> documents =
                    FileSystemDocumentLoader.loadDocuments("src/main/resources/docs");

            if (documents == null || documents.isEmpty()) {
                System.out.println("没有找到文档，跳过初始化");
                return;
            }

            System.out.println("文档数量: " + documents.size());

            //  文档切割
            DocumentByParagraphSplitter splitter =
                    new DocumentByParagraphSplitter(500, 100);

            int success = 0;
            int skipped = 0;

            //  手动 ingest
            for (Document doc : documents) {

                List<TextSegment> segments = splitter.split(doc);

                for (TextSegment segment : segments) {

                    String text = segment.text();

                    // 过滤空文本
                    if (text == null || text.trim().isEmpty()) {
                        skipped++;
                        continue;
                    }

                    String fileName = segment.metadata().getString("file_name");
                    if (fileName == null) {
                        fileName = "unknown";
                    }

                    String finalText = fileName + "\n" + text;

                    try {
                        // 调 embedding
                        var response = embeddingModel.embed(finalText);

                        if (response == null || response.content() == null) {
                            System.out.println("embedding 返回 null");
                            skipped++;
                            continue;
                        }

                        float[] vector = response.content().vector();

                        //  核心校验（关键！！！）
                        if (vector == null || vector.length == 0) {
                            System.out.println("空向量，跳过: " + shortText(finalText));
                            skipped++;
                            continue;
                        }

                        if (vector.length != 1536) {
                            System.out.println("向量维度异常: " + vector.length);
                            skipped++;
                            continue;
                        }

                        //  写入向量库
                        embeddingStore.add(
                                response.content(),
                                TextSegment.from(finalText, segment.metadata())
                        );

                        success++;

                    } catch (Exception e) {
                        System.out.println("embedding失败: " + e.getMessage());
                        skipped++;
                    }
                }
            }

            //  输出结果
            System.out.println(" 成功写入: " + success);
            System.out.println("跳过数据: " + skipped);
            System.out.println("RAG 数据初始化完成！");

        } catch (Exception e) {
            System.err.println("RAG 初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 日志截断（防止日志爆炸）
     */
    private String shortText(String text) {
        if (text == null) return "";
        return text.length() > 50 ? text.substring(0, 50) + "..." : text;
    }
}