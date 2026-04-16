# 快速开始指南

## 🚀 5 分钟快速启动

### 步骤 1：启动 Milvus 向量数据库

#### 方法一：使用启动脚本（推荐）
```bash
# Windows
cd langchain4j
start-milvus.bat
```

#### 方法二：手动启动 Docker
```bash
docker run -d \
  --name milvus-standalone \
  -p 19530:19530 \
  -p 9091:9091 \
  -v /tmp/milvus:/var/lib/milvus \
  milvusdb/milvus:v2.3.0
```

### 步骤 2：准备文档

在项目根目录创建 `docs` 文件夹：
```bash
mkdir docs
```

放入你的 Markdown 文档，例如：
- `docs/java 基础.md`
- `docs/Spring Boot 教程.md`
- `docs/设计模式.md`

### 步骤 3：启动后端服务

```bash
cd langchain4j
mvn spring-boot:run
```

或直接在 IDEA 中运行 `Langchain4jApplication.java`

### 步骤 4：启动前端服务

```bash
cd frontend
npm run dev
```

### 步骤 5：访问应用

打开浏览器访问：http://localhost:5173/

## 📋 验证是否成功

### 1. 检查 Milvus 服务
```bash
docker ps | grep milvus
```
应该看到 `milvus-standalone` 容器正在运行

### 2. 检查后端日志
启动时应该看到：
```
初始化 Milvus 向量数据库配置...
主机地址：localhost:19530
集合名称：langchain4j_collection
Milvus 向量数据库初始化完成！
开始 RAG 数据初始化...
正在从目录加载文档：docs
成功加载 X 个文档
RAG 数据初始化完成！
```

### 3. 测试 RAG 功能
在前端页面提问与文档相关的问题，AI 应该能基于文档内容回答。

## 🔧 常用命令

### Milvus 管理
```bash
# 查看日志
docker logs -f milvus-standalone

# 停止服务
docker stop milvus-standalone

# 重启服务
docker restart milvus-standalone

# 删除容器（会清空数据）
docker rm -f milvus-standalone
```

### 后端服务
```bash
# 清理并重新编译
mvn clean install

# 跳过测试运行
mvn spring-boot:run -DskipTests
```

### 前端服务
```bash
# 安装依赖
npm install

# 开发模式
npm run dev

# 构建生产版本
npm run build
```

## 📁 项目结构

```
langchain4j/
├── docs/                          # 文档目录（自己创建）
│   ├── java 基础.md
│   └── Spring Boot 教程.md
├── src/
│   └── main/
│       ├── java/
│       │   └── cn/langchain4j/
│       │       ├── ai/
│       │       │   ├── config/           # AI 模型配置
│       │       │   ├── rag/              # RAG 相关配置
│       │       │   │   ├── MilvusConfig.java           # Milvus 配置
│       │       │   │   ├── RagContentRetrieverConfig.java  # RAG 检索器配置
│       │       │   │   └── RagDataInitializer.java     # RAG 数据初始化
│       │       │   └── tools/              # AI 工具
│       │       └── controller/         # 控制器
│       └── resources/
│           ├── application-local.yml   # 配置文件
│           └── system-prompt.txt       # 系统提示词
├── frontend/                    # 前端项目
│   ├── src/
│   │   ├── App.vue            # 主页面
│   │   ├── api/               # API 调用
│   │   └── main.js            # 入口文件
│   └── package.json
├── start-milvus.bat           # Milvus 启动脚本
├── MILVUS_INTEGRATION.md      # 详细集成文档
└── README.md                  # 本文件
```

## 🎯 下一步

1. **添加更多文档**：将你的学习资料放入 `docs` 目录
2. **调整配置**：根据需要修改 `application-local.yml` 中的参数
3. **自定义提示词**：编辑 `system-prompt.txt` 改变 AI 行为
4. **开发新功能**：在 `AiCodeHelperService` 中添加新的 AI 能力

## 🆘 常见问题

### Q: Milvus 启动失败？
A: 确保 Docker Desktop 已启动，端口 19530 未被占用

### Q: 文档没有加载？
A: 检查 `docs` 目录是否存在，是否有文档文件

### Q: AI 回答不准确？
A: 调整 `application-local.yml` 中的 `rag.min-score` 参数（建议 0.7-0.8）

### Q: 前端无法连接后端？
A: 确保后端在 8081 端口运行，前端代理配置正确

## 📚 更多文档

- [Milvus 详细集成文档](MILVUS_INTEGRATION.md)
- [LangChain4j 官方文档](https://docs.langchain4j.dev/)
- [Milvus 官方文档](https://milvus.io/docs)
