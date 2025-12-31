# AI Assistant - Java AI应用开发示例项目

基于Spring Boot 3.x + JDK 17的AI应用开发示例，集成Dify和LangChain4j。

## 技术栈

- **Java 17** - 最新LTS版本
- **Spring Boot 3.2.1** - Web框架
- **LangChain4j 0.27.1** - Java版LangChain
- **OkHttp 4.12.0** - HTTP客户端
- **Swagger/OpenAPI 3** - API文档

## 功能特性

- 🤖 **多渠道聊天** - 支持Dify和LangChain4j两种AI对话方式
- 🔧 **AI Agent** - 带工具调用的智能助手
- 📚 **RAG问答** - 知识库检索增强生成
- 🔄 **流式响应** - 支持SSE流式输出
- 📖 **API文档** - 自动生成Swagger文档

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- Docker (可选，用于运行Dify)

### 配置

1. 复制配置文件：
```bash
cp src/main/resources/application.yml src/main/resources/application-local.yml
```

2. 编辑 `application-local.yml`，配置API密钥：
```yaml
ai:
  dify:
    api-key: your-dify-api-key
  openai:
    api-key: your-openai-api-key
```

或者设置环境变量：
```bash
set DIFY_API_KEY=your-dify-api-key
set OPENAI_API_KEY=your-openai-api-key
```

### 构建和运行

```bash
# 构建项目
mvn clean package -DskipTests

# 运行项目
mvn spring-boot:run

# 或者运行jar包
java -jar target/ai-assistant-1.0.0-SNAPSHOT.jar
```

### 访问

- API文档: http://localhost:8080/swagger-ui.html
- 健康检查: http://localhost:8080/actuator/health

## API接口

### 聊天接口

**Dify聊天**
```bash
curl -X POST http://localhost:8080/api/chat/dify \
  -H "Content-Type: application/json" \
  -d '{
    "message": "什么是Spring Boot？",
    "userId": "user-123"
  }'
```

**LangChain聊天**
```bash
curl -X POST http://localhost:8080/api/chat/langchain \
  -H "Content-Type: application/json" \
  -d '{
    "message": "请解释Java Stream API",
    "userId": "user-123"
  }'
```

**流式聊天**
```bash
curl -X POST http://localhost:8080/api/chat/langchain/stream \
  -H "Content-Type: application/json" \
  -H "Accept: text/event-stream" \
  -d '{
    "message": "写一个冒泡排序算法"
  }'
```

### Agent接口

**Java开发助手**
```bash
curl -X POST "http://localhost:8080/api/agent/java-assist?question=ArrayList和LinkedList的区别"
```

**代码审查**
```bash
curl -X POST http://localhost:8080/api/agent/code-review \
  -H "Content-Type: text/plain" \
  -d 'public void test() { System.out.println("hello"); }'
```

### RAG接口

**上传文档**
```bash
curl -X POST http://localhost:8080/api/rag/upload \
  -F "file=@document.pdf"
```

**知识库问答**
```bash
curl -X POST http://localhost:8080/api/rag/query \
  -H "Content-Type: application/json" \
  -d '{"question": "文档中提到了什么？"}'
```

## 项目结构

```
ai-assistant/
├── src/main/java/com/example/ai/
│   ├── AiAssistantApplication.java    # 启动类
│   ├── config/                        # 配置类
│   │   ├── AiProperties.java          # AI配置属性
│   │   ├── LangChainConfig.java       # LangChain4j配置
│   │   ├── OkHttpConfig.java          # HTTP客户端配置
│   │   └── SwaggerConfig.java         # API文档配置
│   ├── client/                        # 外部服务客户端
│   │   └── DifyClient.java            # Dify API客户端
│   ├── controller/                    # REST控制器
│   │   ├── ChatController.java        # 聊天接口
│   │   ├── AgentController.java       # Agent接口
│   │   └── RagController.java         # RAG接口
│   ├── service/                       # 业务服务
│   │   ├── ChatService.java           # 聊天服务接口
│   │   ├── AgentService.java          # Agent服务接口
│   │   └── impl/                      # 服务实现
│   ├── dto/                           # 数据传输对象
│   ├── tools/                         # AI工具
│   │   └── JavaDevTools.java          # Java开发工具
│   ├── exception/                     # 异常处理
│   └── util/                          # 工具类
├── src/main/resources/
│   └── application.yml                # 配置文件
├── src/test/                          # 测试代码
└── pom.xml                            # Maven配置
```

## 扩展开发

### 添加新的AI工具

```java
@Component
public class MyCustomTools {
    
    @Tool("工具描述")
    public String myTool(String input) {
        // 工具实现
        return "结果";
    }
}
```

### 添加新的Agent类型

1. 在 `AgentRequest.AgentType` 中添加新类型
2. 创建新的Agent服务实现
3. 在Controller中添加对应接口

### 集成其他LLM

修改 `LangChainConfig.java`，使用LangChain4j提供的其他模型：

```java
// 使用Anthropic Claude
@Bean
public ChatLanguageModel anthropicModel() {
    return AnthropicChatModel.builder()
            .apiKey(apiKey)
            .modelName("claude-3-opus-20240229")
            .build();
}
```

## 常见问题

**Q: 如何在国内使用OpenAI API？**

A: 可以配置代理或使用国内的API代理服务：
```yaml
ai:
  openai:
    base-url: https://your-proxy.com/v1
```

**Q: 如何降低API调用成本？**

A: 
1. 使用更便宜的模型（如gpt-3.5-turbo）
2. 减少max-tokens
3. 实现本地缓存

**Q: 如何添加用户认证？**

A: 添加Spring Security依赖并配置：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

## License

MIT License
