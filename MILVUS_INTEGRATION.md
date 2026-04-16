# Milvus 向量数据库集成说明

## 📋 概述

本项目已集成 Milvus 向量数据库，用于 RAG（检索增强生成）功能。Milvus 是一个开源的向量数据库，专门用于存储和检索大规模向量数据。

## 🏗️ 架构组成

### 1. **MilvusConfig.java** - Milvus 配置类
- 配置 Milvus 连接参数（主机、端口、集合名称等）
- 创建 `EmbeddingStore` Bean
- 支持认证模式（可选）

### 2. **RagContentRetrieverConfig.java** - RAG 检索器配置
- 配置内容检索策略
- 设置最大结果数和最小相似度分数
- 创建 `ContentRetriever` Bean

### 3. **RagDataInitializer.java** - 数据初始化器
- 应用启动时自动加载文档
- 将文档向量化并存入 Milvus
- 支持自定义文档目录

## 🚀 快速开始

### 1. 启动 Milvus 服务

#### 使用 Docker（推荐）
```bash
# 下载 Milvus Docker 镜像
docker pull milvusdb/milvus:v2.3.0

# 创建数据目录
mkdir -p /tmp/milvus

# 启动 Milvus 容器
docker run -d \
  --name milvus-standalone \
  -p 19530:19530 \
  -p 9091:9091 \
  -v /tmp/milvus:/var/lib/milvus \
  milvusdb/milvus:v2.3.0
```

#### 使用 Docker Compose
```yaml
# docker-compose.yml
version: '3.5'
services:
  etcd:
    image: quay.io/coreos/etcd:v3.5.0
    environment:
      - ETCD_AUTO_COMPACTION_MODE=revision
      - ETCD_AUTO_COMPACTION_RETENTION=1000
      - ETCD_QUOTA_BACKEND_BYTES=4294967296
    volumes:
      - ${DOCKER_VOLUME_DIRECTORY:-.}/volumes/etcd:/etcd
    command: etcd -advertise-client-urls=http://127.0.0.1:2379 -listen-client-urls http://0.0.0.0:2379 --data-dir /etcd

  minio:
    image: minio/minio:RELEASE.2020-12-03T00-03-10Z
    environment:
      MINIO_ACCESS_KEY: minioadmin
      MINIO_SECRET_KEY: minioadmin
    volumes:
      - ${DOCKER_VOLUME_DIRECTORY:-.}/volumes/minio:/minio_data
    command: minio server /minio_data
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:9000/minio/health/live"]
      interval: 30s
      timeout: 20s
      retries: 3

  milvus-standalone:
    image: milvusdb/milvus:v2.3.0
    command: ["milvus", "run", "standalone"]
    environment:
      ETCD_ENDPOINTS: etcd:2379
      MINIO_ADDRESS: minio:9000
    volumes:
      - ${DOCKER_VOLUME_DIRECTORY:-.}/volumes/milvus:/var/lib/milvus
    ports:
      - "19530:19530"
      - "9091:9091"
    depends_on:
      - "etcd"
      - "minio"

networks:
  default:
    name: milvus
```

然后执行：
```bash
docker-compose up -d
```

### 2. 准备文档

在项目根目录创建 `docs` 文件夹，放入你的文档：
```
langchain4j/
├── docs/
│   ├── java 基础.md
│   ├── Spring Boot 教程.md
│   └── ...
└── src/
```

### 3. 配置参数

编辑 `application-local.yml`：

```yaml
# Milvus 配置
milvus:
  host: localhost          # Milvus 主机地址
  port: 19530             # Milvus 端口
  collection-name: langchain4j_collection  # 集合名称
  # username: your_username  # 可选：用户名
  # password: your_password  # 可选：密码

# RAG 配置
rag:
  document-directory: docs   # 文档目录
  auto-initialize: true      # 是否自动初始化
  max-results: 5            # 最大检索结果数
  min-score: 0.75           # 最小相似度分数
```

### 4. 启动应用

运行 Spring Boot 应用，系统会自动：
1. 连接 Milvus 数据库
2. 加载 `docs` 目录下的文档
3. 将文档向量化并存入 Milvus
4. 配置 RAG 检索器

## 📝 配置参数说明

### Milvus 参数
| 参数 | 默认值 | 说明 |
|------|--------|------|
| `milvus.host` | localhost | Milvus 服务器地址 |
| `milvus.port` | 19530 | Milvus 服务端口 |
| `milvus.collection-name` | langchain4j_collection | 向量集合名称 |
| `milvus.username` | - | 用户名（可选） |
| `milvus.password` | - | 密码（可选） |

### RAG 参数
| 参数 | 默认值 | 说明 |
|------|--------|------|
| `rag.document-directory` | docs | 文档目录路径 |
| `rag.auto-initialize` | true | 是否自动初始化 |
| `rag.max-results` | 5 | 最大检索结果数量 |
| `rag.min-score` | 0.75 | 最小相似度分数（0-1） |

## 🔍 使用示例

### 在 AI 服务中使用 RAG

```java
@Service
public class AiService {
    
    @Resource
    private AiCodeHelperService aiCodeHelperService;
    
    public String chat(String userMessage) {
        // AI 会自动从 Milvus 检索相关内容并回答
        return aiCodeHelperService.chatStream(1, userMessage);
    }
}
```

### 手动添加文档到向量库

```java
@Service
public class DocumentService {
    
    @Resource
    private EmbeddingModel embeddingModel;
    
    @Resource
    private EmbeddingStore embeddingStore;
    
    public void addDocument(String text) {
        // 1. 创建文本段
        TextSegment segment = TextSegment.from(text);
        
        // 2. 生成向量
        Embedding embedding = embeddingModel.embed(segment).content();
        
        // 3. 存储到 Milvus
        embeddingStore.add(embedding, segment);
    }
}
```

## 🛠️ 故障排查

### 1. 连接失败
```
错误：ConnectException: Connection refused
解决：检查 Milvus 服务是否启动
docker ps | grep milvus
```

### 2. 维度不匹配
```
错误：IllegalArgumentException: Length of vector a (0) must be equal to the length of vector b (1536)
解决：删除并重建集合
```

### 3. 文档加载失败
```
错误：FileNotFoundException
解决：检查文档目录路径是否正确
```

## 📊 监控和管理

### 查看集合信息
```bash
# 进入 Milvus 容器
docker exec -it milvus-standalone bash

# 使用 Milvus 客户端连接
milvus-cli connect -h localhost -p 19530

# 查看集合
milvus-cli show collections
```

### 清空集合数据
```java
@Bean
public CommandLineRunner clearCollection(EmbeddingStore embeddingStore) {
    return args -> {
        // 清空集合（谨慎使用）
        if (embeddingStore instanceof MilvusEmbeddingStore) {
            ((MilvusEmbeddingStore) embeddingStore).clear();
        }
    };
}
```

## 🎯 最佳实践

1. **文档分割**：使用段落分割器，保持上下文完整性
2. **相似度阈值**：设置为 0.7-0.8，平衡准确性和召回率
3. **批量处理**：大量文档时使用批量加载
4. **定期清理**：定期清理过期或无用数据
5. **备份恢复**：定期备份 Milvus 数据目录

## 🔗 相关资源

- [Milvus 官方文档](https://milvus.io/docs)
- [LangChain4j RAG 文档](https://docs.langchain4j.dev/)
- [Milvus GitHub](https://github.com/milvus-io/milvus)
