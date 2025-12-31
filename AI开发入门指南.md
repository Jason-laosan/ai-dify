# AI应用开发入门指南 - Java开发者转型

本指南专为Java开发工程师设计，帮助你快速上手Dify和LangGraph进行AI应用开发。

---

## 目录
1. [Dify入门指南](#dify入门指南)
2. [LangGraph入门指南](#langgraph入门指南)
3. [Java集成示例](#java集成示例)
4. [推荐学习路径](#推荐学习路径)

---

## Dify入门指南

### 什么是Dify？
Dify是一个开源的LLM应用开发平台，提供可视化界面来构建AI应用，无需深入了解底层技术即可快速开发。

### 优势
- ✅ 可视化工作流编辑器
- ✅ 内置提示词工程工具
- ✅ 支持多种LLM模型
- ✅ 提供API接口，方便Java集成
- ✅ 适合快速原型开发

### Windows环境安装步骤

#### 方式一：Docker安装（推荐）

**前置要求：**
- Docker Desktop for Windows
- WSL2（Windows Subsystem for Linux 2）

**步骤1：安装Docker Desktop**
```powershell
# 下载Docker Desktop
# 访问：https://www.docker.com/products/docker-desktop/

# 安装后启动Docker Desktop，确保WSL2集成已启用
```

**步骤2：克隆Dify仓库**
```powershell
# 打开PowerShell或Windows Terminal
cd C:\Users\kevinJin\Documents\github

# 克隆Dify仓库
git clone https://github.com/langgenius/dify.git
cd dify\docker
```

**步骤3：启动Dify**
```powershell
# 复制环境变量配置文件
cp .env.example .env

# 启动所有服务（首次启动会下载镜像，需要等待）
docker-compose up -d

# 查看服务状态
docker-compose ps
```

**步骤4：访问Dify**
- 打开浏览器访问：`http://localhost/install`
- 设置管理员账号和密码
- 开始使用Dify

**步骤5：停止和重启**
```powershell
# 停止服务
docker-compose down

# 重启服务
docker-compose up -d

# 查看日志
docker-compose logs -f
```

#### 方式二：源码安装

**前置要求：**
- Python 3.10+
- Node.js 18+
- PostgreSQL 15+
- Redis 7+

**步骤1：安装Python依赖**
```powershell
# 克隆仓库
git clone https://github.com/langgenius/dify.git
cd dify

# 创建虚拟环境
python -m venv venv
.\venv\Scripts\Activate.ps1

# 安装后端依赖
cd api
pip install -r requirements.txt
```

**步骤2：配置数据库**
```powershell
# 安装PostgreSQL（使用Chocolatey）
choco install postgresql

# 创建数据库
psql -U postgres
CREATE DATABASE dify;
CREATE USER dify_user WITH PASSWORD 'dify_password';
GRANT ALL PRIVILEGES ON DATABASE dify TO dify_user;
\q
```

**步骤3：配置环境变量**
```powershell
# 复制配置文件
cd api
cp .env.example .env

# 编辑.env文件，配置数据库连接
# DB_USERNAME=dify_user
# DB_PASSWORD=dify_password
# DB_HOST=localhost
# DB_PORT=5432
# DB_DATABASE=dify
```

**步骤4：初始化数据库**
```powershell
# 运行数据库迁移
flask db upgrade
```

**步骤5：启动服务**
```powershell
# 启动后端（在api目录）
flask run --host 0.0.0.0 --port 5001

# 新开一个终端，启动前端
cd ..\web
npm install
npm run dev
```

### Dify核心概念

#### 1. 应用类型
- **聊天助手**：对话式AI应用
- **文本生成**：内容生成应用
- **Agent**：具有工具调用能力的智能体
- **工作流**：复杂的多步骤AI流程

#### 2. 工作流节点
- **LLM节点**：调用大语言模型
- **知识检索**：从知识库检索相关信息
- **条件分支**：根据条件执行不同路径
- **代码执行**：运行自定义Python/JavaScript代码
- **HTTP请求**：调用外部API
- **变量聚合**：处理和转换数据

#### 3. 知识库管理
- 支持文档上传（PDF、Word、Markdown等）
- 自动分段和向量化
- 支持多种嵌入模型

### Dify快速上手示例

#### 示例1：创建简单的聊天机器人

1. 登录Dify控制台
2. 点击"创建应用" → 选择"聊天助手"
3. 配置模型：选择OpenAI GPT-4或其他模型
4. 设置系统提示词：
   ```
   你是一个专业的Java开发助手，擅长解答Java相关的技术问题。
   请用简洁、专业的语言回答用户的问题。
   ```
5. 点击"发布"，获取API密钥

#### 示例2：创建知识库问答应用

1. 创建知识库：上传Java技术文档
2. 创建新应用 → 选择"聊天助手"
3. 在应用设置中关联知识库
4. 配置检索设置：
   - 检索模式：向量检索
   - Top K：3
   - 相似度阈值：0.7
5. 测试和发布

### Dify API调用

获取API密钥后，可以通过HTTP请求调用：

```bash
# 测试API调用
curl -X POST 'http://localhost/v1/chat-messages' \
  -H 'Authorization: Bearer YOUR_API_KEY' \
  -H 'Content-Type: application/json' \
  -d '{
    "inputs": {},
    "query": "什么是Spring Boot？",
    "response_mode": "blocking",
    "user": "user-123"
  }'
```

---

## LangGraph入门指南

### 什么是LangGraph？
LangGraph是LangChain团队开发的用于构建有状态、多参与者AI应用的框架，特别适合构建复杂的Agent系统。

### 优势
- ✅ 强大的状态管理
- ✅ 支持复杂的工作流图
- ✅ 循环和条件分支
- ✅ 人机协作（Human-in-the-loop）
- ✅ 适合复杂的AI应用开发

### Windows环境安装步骤

#### 步骤1：安装Python环境

```powershell
# 检查Python版本（需要3.9+）
python --version

# 如果没有安装Python，使用Chocolatey安装
choco install python --version=3.11.0

# 或者从官网下载：https://www.python.org/downloads/
```

#### 步骤2：创建项目目录

```powershell
# 创建项目目录
cd C:\Users\kevinJin\Documents\github\ai-dify
mkdir langgraph-demo
cd langgraph-demo

# 创建虚拟环境
python -m venv venv

# 激活虚拟环境
.\venv\Scripts\Activate.ps1
```

#### 步骤3：安装LangGraph和依赖

```powershell
# 安装LangGraph
pip install langgraph

# 安装LangChain核心库
pip install langchain langchain-openai langchain-community

# 安装其他常用依赖
pip install python-dotenv requests beautifulsoup4

# 生成requirements.txt
pip freeze > requirements.txt
```

#### 步骤4：配置环境变量

```powershell
# 创建.env文件
New-Item -Path .env -ItemType File

# 编辑.env文件，添加API密钥
# OPENAI_API_KEY=your_openai_api_key
# ANTHROPIC_API_KEY=your_anthropic_api_key
```

使用记事本或VS Code编辑`.env`文件：
```env
OPENAI_API_KEY=sk-your-key-here
ANTHROPIC_API_KEY=sk-ant-your-key-here
LANGCHAIN_TRACING_V2=true
LANGCHAIN_API_KEY=your-langsmith-key
```

### LangGraph核心概念

#### 1. 状态图（StateGraph）
LangGraph的核心是状态图，定义了AI应用的状态和转换逻辑。

#### 2. 节点（Nodes）
- 每个节点是一个函数，接收状态并返回更新
- 节点可以调用LLM、工具或执行任何Python代码

#### 3. 边（Edges）
- **普通边**：无条件转换
- **条件边**：根据状态决定下一个节点

#### 4. 状态（State）
- 使用TypedDict定义状态结构
- 状态在节点间传递和更新

### LangGraph快速上手示例

#### 示例1：简单的对话Agent

创建文件：`simple_agent.py`

```python
import os
from typing import TypedDict, Annotated
from langgraph.graph import StateGraph, END
from langchain_openai import ChatOpenAI
from langchain_core.messages import HumanMessage, AIMessage
from dotenv import load_dotenv

# 加载环境变量
load_dotenv()

# 定义状态
class AgentState(TypedDict):
    messages: list
    next_action: str

# 初始化LLM
llm = ChatOpenAI(model="gpt-4", temperature=0)

# 定义节点函数
def chatbot_node(state: AgentState) -> AgentState:
    """处理用户消息的节点"""
    messages = state["messages"]
    response = llm.invoke(messages)
    return {
        "messages": messages + [response],
        "next_action": "end"
    }

# 创建状态图
workflow = StateGraph(AgentState)

# 添加节点
workflow.add_node("chatbot", chatbot_node)

# 设置入口点
workflow.set_entry_point("chatbot")

# 添加边
workflow.add_edge("chatbot", END)

# 编译图
app = workflow.compile()

# 使用示例
if __name__ == "__main__":
    initial_state = {
        "messages": [HumanMessage(content="什么是Spring Boot？")],
        "next_action": ""
    }
    
    result = app.invoke(initial_state)
    print(result["messages"][-1].content)
```

运行示例：
```powershell
python simple_agent.py
```

#### 示例2：带工具调用的Agent

创建文件：`tool_agent.py`

```python
import os
from typing import TypedDict, Literal
from langgraph.graph import StateGraph, END
from langchain_openai import ChatOpenAI
from langchain_core.messages import HumanMessage, AIMessage, ToolMessage
from langchain_core.tools import tool
from dotenv import load_dotenv

load_dotenv()

# 定义工具
@tool
def search_java_docs(query: str) -> str:
    """搜索Java文档"""
    # 这里是模拟实现，实际应该调用真实的搜索API
    return f"关于'{query}'的Java文档搜索结果：Spring Boot是一个快速开发框架..."

@tool
def calculate(expression: str) -> str:
    """计算数学表达式"""
    try:
        result = eval(expression)
        return f"计算结果：{result}"
    except Exception as e:
        return f"计算错误：{str(e)}"

tools = [search_java_docs, calculate]

# 定义状态
class AgentState(TypedDict):
    messages: list
    next_step: str

# 初始化LLM并绑定工具
llm = ChatOpenAI(model="gpt-4", temperature=0)
llm_with_tools = llm.bind_tools(tools)

# Agent节点
def agent_node(state: AgentState) -> AgentState:
    """Agent决策节点"""
    messages = state["messages"]
    response = llm_with_tools.invoke(messages)
    return {
        "messages": messages + [response],
        "next_step": "tools" if response.tool_calls else "end"
    }

# 工具执行节点
def tool_node(state: AgentState) -> AgentState:
    """执行工具调用"""
    messages = state["messages"]
    last_message = messages[-1]
    
    tool_results = []
    for tool_call in last_message.tool_calls:
        tool_name = tool_call["name"]
        tool_args = tool_call["args"]
        
        # 查找并执行工具
        for tool in tools:
            if tool.name == tool_name:
                result = tool.invoke(tool_args)
                tool_results.append(
                    ToolMessage(
                        content=result,
                        tool_call_id=tool_call["id"]
                    )
                )
    
    return {
        "messages": messages + tool_results,
        "next_step": "agent"
    }

# 路由函数
def should_continue(state: AgentState) -> Literal["tools", "end"]:
    """决定下一步"""
    return state["next_step"]

# 构建图
workflow = StateGraph(AgentState)

# 添加节点
workflow.add_node("agent", agent_node)
workflow.add_node("tools", tool_node)

# 设置入口
workflow.set_entry_point("agent")

# 添加条件边
workflow.add_conditional_edges(
    "agent",
    should_continue,
    {
        "tools": "tools",
        "end": END
    }
)

# 工具执行后返回agent
workflow.add_edge("tools", "agent")

# 编译
app = workflow.compile()

# 使用示例
if __name__ == "__main__":
    initial_state = {
        "messages": [HumanMessage(content="帮我搜索Spring Boot的相关信息，然后计算100+200")],
        "next_step": ""
    }
    
    result = app.invoke(initial_state)
    for msg in result["messages"]:
        print(f"{msg.__class__.__name__}: {msg.content}\n")
```

运行示例：
```powershell
python tool_agent.py
```

#### 示例3：多Agent协作系统

创建文件：`multi_agent.py`

```python
import os
from typing import TypedDict, Literal
from langgraph.graph import StateGraph, END
from langchain_openai import ChatOpenAI
from langchain_core.messages import HumanMessage, SystemMessage
from dotenv import load_dotenv

load_dotenv()

# 定义状态
class MultiAgentState(TypedDict):
    messages: list
    current_agent: str
    task_complete: bool

# 初始化不同角色的LLM
researcher_llm = ChatOpenAI(model="gpt-4", temperature=0.7)
coder_llm = ChatOpenAI(model="gpt-4", temperature=0)
reviewer_llm = ChatOpenAI(model="gpt-4", temperature=0.3)

# 研究员Agent
def researcher_node(state: MultiAgentState) -> MultiAgentState:
    """研究员：负责需求分析和技术调研"""
    system_msg = SystemMessage(content="你是一个技术研究员，负责分析需求并提供技术方案建议。")
    messages = [system_msg] + state["messages"]
    response = researcher_llm.invoke(messages)
    
    return {
        "messages": state["messages"] + [response],
        "current_agent": "coder",
        "task_complete": False
    }

# 编码员Agent
def coder_node(state: MultiAgentState) -> MultiAgentState:
    """编码员：负责编写代码"""
    system_msg = SystemMessage(content="你是一个Java开发工程师，根据需求编写高质量的代码。")
    messages = [system_msg] + state["messages"]
    response = coder_llm.invoke(messages)
    
    return {
        "messages": state["messages"] + [response],
        "current_agent": "reviewer",
        "task_complete": False
    }

# 审查员Agent
def reviewer_node(state: MultiAgentState) -> MultiAgentState:
    """审查员：负责代码审查"""
    system_msg = SystemMessage(content="你是一个代码审查员，检查代码质量并提供改进建议。")
    messages = [system_msg] + state["messages"]
    response = reviewer_llm.invoke(messages)
    
    return {
        "messages": state["messages"] + [response],
        "current_agent": "end",
        "task_complete": True
    }

# 路由函数
def route_agent(state: MultiAgentState) -> Literal["researcher", "coder", "reviewer", "end"]:
    """根据当前状态路由到下一个Agent"""
    if state["task_complete"]:
        return "end"
    return state["current_agent"]

# 构建图
workflow = StateGraph(MultiAgentState)

# 添加节点
workflow.add_node("researcher", researcher_node)
workflow.add_node("coder", coder_node)
workflow.add_node("reviewer", reviewer_node)

# 设置入口
workflow.set_entry_point("researcher")

# 添加边
workflow.add_conditional_edges(
    "researcher",
    route_agent,
    {
        "coder": "coder",
        "end": END
    }
)

workflow.add_conditional_edges(
    "coder",
    route_agent,
    {
        "reviewer": "reviewer",
        "end": END
    }
)

workflow.add_conditional_edges(
    "reviewer",
    route_agent,
    {
        "end": END
    }
)

# 编译
app = workflow.compile()

# 使用示例
if __name__ == "__main__":
    initial_state = {
        "messages": [HumanMessage(content="我需要创建一个Spring Boot的REST API，用于管理用户信息")],
        "current_agent": "researcher",
        "task_complete": False
    }
    
    result = app.invoke(initial_state)
    
    print("=== 多Agent协作结果 ===\n")
    for i, msg in enumerate(result["messages"]):
        print(f"步骤 {i+1}: {msg.content[:200]}...\n")
```

运行示例：
```powershell
python multi_agent.py
```

### LangGraph进阶功能

#### 1. 持久化状态

```python
from langgraph.checkpoint.sqlite import SqliteSaver

# 使用SQLite保存检查点
memory = SqliteSaver.from_conn_string("checkpoints.db")
app = workflow.compile(checkpointer=memory)

# 使用线程ID来保持会话
config = {"configurable": {"thread_id": "user-123"}}
result = app.invoke(initial_state, config)
```

#### 2. 流式输出

```python
# 流式处理每个节点的输出
for chunk in app.stream(initial_state):
    print(chunk)
```

#### 3. 人机协作

```python
from langgraph.checkpoint.sqlite import SqliteSaver
from langgraph.graph import StateGraph

# 添加中断点
workflow.add_node("human_review", human_review_node)
workflow.add_edge("agent", "human_review")

# 编译时启用中断
app = workflow.compile(
    checkpointer=SqliteSaver.from_conn_string("checkpoints.db"),
    interrupt_before=["human_review"]
)

# 运行到中断点
result = app.invoke(initial_state, config)

# 人工审查后继续
app.invoke(None, config)  # 继续执行
```

---

## Java集成示例

### 方式一：通过Dify API集成

创建Maven项目结构：

```powershell
# 在当前项目中创建Maven模块
mkdir dify-java-client
cd dify-java-client
```

创建`pom.xml`：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>dify-java-client</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>Dify Java Client</name>
    <description>Java client for Dify AI platform</description>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- OkHttp for HTTP requests -->
        <dependency>
            <groupId>com.squareup.okhttp3</groupId>
            <artifactId>okhttp</artifactId>
            <version>4.12.0</version>
        </dependency>

        <!-- Jackson for JSON processing -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.16.0</version>
        </dependency>

        <!-- Lombok for reducing boilerplate -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.30</version>
            <scope>provided</scope>
        </dependency>

        <!-- SLF4J for logging -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.9</version>
        </dependency>

        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.4.14</version>
        </dependency>

        <!-- JUnit for testing -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.1</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

创建Dify客户端类：`src/main/java/com/example/dify/DifyClient.java`

```java
package com.example.dify;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DifyClient {
    
    private final String apiKey;
    private final String baseUrl;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public DifyClient(String apiKey, String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.objectMapper = new ObjectMapper();
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }
    
    /**
     * 发送聊天消息（阻塞模式）
     */
    public ChatResponse sendMessage(ChatRequest request) throws IOException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("inputs", request.getInputs());
        requestBody.put("query", request.getQuery());
        requestBody.put("response_mode", "blocking");
        requestBody.put("user", request.getUser());
        requestBody.put("conversation_id", request.getConversationId());
        
        String jsonBody = objectMapper.writeValueAsString(requestBody);
        
        Request httpRequest = new Request.Builder()
                .url(baseUrl + "/v1/chat-messages")
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .build();
        
        try (Response response = httpClient.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response);
            }
            
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, ChatResponse.class);
        }
    }
    
    /**
     * 发送聊天消息（流式模式）
     */
    public void sendMessageStream(ChatRequest request, StreamCallback callback) throws IOException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("inputs", request.getInputs());
        requestBody.put("query", request.getQuery());
        requestBody.put("response_mode", "streaming");
        requestBody.put("user", request.getUser());
        requestBody.put("conversation_id", request.getConversationId());
        
        String jsonBody = objectMapper.writeValueAsString(requestBody);
        
        Request httpRequest = new Request.Builder()
                .url(baseUrl + "/v1/chat-messages")
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .build();
        
        try (Response response = httpClient.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response);
            }
            
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String line;
                while ((line = responseBody.source().readUtf8Line()) != null) {
                    if (line.startsWith("data: ")) {
                        String data = line.substring(6);
                        callback.onMessage(data);
                    }
                }
                callback.onComplete();
            }
        }
    }
    
    /**
     * 获取对话历史
     */
    public ConversationHistory getConversationHistory(String conversationId, String user) throws IOException {
        HttpUrl url = HttpUrl.parse(baseUrl + "/v1/messages")
                .newBuilder()
                .addQueryParameter("conversation_id", conversationId)
                .addQueryParameter("user", user)
                .build();
        
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + apiKey)
                .get()
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response);
            }
            
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, ConversationHistory.class);
        }
    }
    
    @Data
    @Builder
    public static class ChatRequest {
        private Map<String, Object> inputs;
        private String query;
        private String user;
        private String conversationId;
    }
    
    @Data
    public static class ChatResponse {
        private String answer;
        private String conversationId;
        private String messageId;
        private Map<String, Object> metadata;
    }
    
    @Data
    public static class ConversationHistory {
        private java.util.List<Message> data;
        private int total;
        
        @Data
        public static class Message {
            private String id;
            private String query;
            private String answer;
            private long createdAt;
        }
    }
    
    public interface StreamCallback {
        void onMessage(String message);
        void onComplete();
        void onError(Exception e);
    }
}
```

创建使用示例：`src/main/java/com/example/dify/DifyExample.java`

```java
package com.example.dify;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DifyExample {
    
    public static void main(String[] args) {
        // 初始化Dify客户端
        String apiKey = System.getenv("DIFY_API_KEY");
        String baseUrl = "http://localhost";
        
        DifyClient client = new DifyClient(apiKey, baseUrl);
        
        // 示例1：发送简单消息
        try {
            DifyClient.ChatRequest request = DifyClient.ChatRequest.builder()
                    .inputs(new HashMap<>())
                    .query("什么是Spring Boot？")
                    .user("user-123")
                    .build();
            
            DifyClient.ChatResponse response = client.sendMessage(request);
            log.info("AI回复: {}", response.getAnswer());
            log.info("对话ID: {}", response.getConversationId());
            
        } catch (Exception e) {
            log.error("发送消息失败", e);
        }
        
        // 示例2：流式对话
        try {
            DifyClient.ChatRequest request = DifyClient.ChatRequest.builder()
                    .inputs(new HashMap<>())
                    .query("请详细解释Spring Boot的自动配置原理")
                    .user("user-123")
                    .build();
            
            client.sendMessageStream(request, new DifyClient.StreamCallback() {
                @Override
                public void onMessage(String message) {
                    System.out.print(message);
                }
                
                @Override
                public void onComplete() {
                    System.out.println("\n[对话完成]");
                }
                
                @Override
                public void onError(Exception e) {
                    log.error("流式对话错误", e);
                }
            });
            
        } catch (Exception e) {
            log.error("流式对话失败", e);
        }
        
        // 示例3：带上下文的对话
        try {
            Map<String, Object> inputs = new HashMap<>();
            inputs.put("context", "我们正在讨论Spring框架");
            
            DifyClient.ChatRequest request = DifyClient.ChatRequest.builder()
                    .inputs(inputs)
                    .query("它的核心特性有哪些？")
                    .user("user-123")
                    .conversationId("previous-conversation-id")
                    .build();
            
            DifyClient.ChatResponse response = client.sendMessage(request);
            log.info("AI回复: {}", response.getAnswer());
            
        } catch (Exception e) {
            log.error("发送消息失败", e);
        }
    }
}
```

### 方式二：通过LangChain4j集成LangGraph

创建`pom.xml`（LangChain4j项目）：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>langchain4j-demo</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <langchain4j.version>0.27.1</langchain4j.version>
    </properties>

    <dependencies>
        <!-- LangChain4j Core -->
        <dependency>
            <groupId>dev.langchain4j</groupId>
            <artifactId>langchain4j</artifactId>
            <version>${langchain4j.version}</version>
        </dependency>

        <!-- OpenAI Integration -->
        <dependency>
            <groupId>dev.langchain4j</groupId>
            <artifactId>langchain4j-open-ai</artifactId>
            <version>${langchain4j.version}</version>
        </dependency>

        <!-- Embeddings -->
        <dependency>
            <groupId>dev.langchain4j</groupId>
            <artifactId>langchain4j-embeddings-all-minilm-l6-v2</artifactId>
            <version>${langchain4j.version}</version>
        </dependency>

        <!-- In-memory vector store -->
        <dependency>
            <groupId>dev.langchain4j</groupId>
            <artifactId>langchain4j-embeddings</artifactId>
            <version>${langchain4j.version}</version>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.30</version>
            <scope>provided</scope>
        </dependency>

        <!-- Logging -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.9</version>
        </dependency>

        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.4.14</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
            </plugin>
        </plugins>
    </build>
</project>
```

创建LangChain4j示例：`src/main/java/com/example/langchain/LangChainExample.java`

```java
package com.example.langchain;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4;

@Slf4j
public class LangChainExample {
    
    public static void main(String[] args) {
        // 初始化OpenAI模型
        String apiKey = System.getenv("OPENAI_API_KEY");
        
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(GPT_4)
                .temperature(0.7)
                .build();
        
        // 创建带记忆的对话链
        ConversationalChain chain = ConversationalChain.builder()
                .chatLanguageModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
        
        // 进行对话
        String response1 = chain.execute("你好，我是一名Java开发工程师");
        log.info("AI: {}", response1);
        
        String response2 = chain.execute("我想学习AI应用开发，有什么建议吗？");
        log.info("AI: {}", response2);
        
        String response3 = chain.execute("我刚才说我是做什么的？");
        log.info("AI: {}", response3);
    }
}
```

创建带工具的Agent示例：`src/main/java/com/example/langchain/AgentWithToolsExample.java`

```java
package com.example.langchain;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4;

@Slf4j
public class AgentWithToolsExample {
    
    static class JavaDocTools {
        
        @Tool("搜索Java文档中的类或方法信息")
        public String searchJavaDoc(String className) {
            log.info("搜索Java文档: {}", className);
            return String.format("找到类 %s 的文档：这是一个Java标准库类...", className);
        }
        
        @Tool("获取Spring Boot配置属性说明")
        public String getSpringBootProperty(String propertyName) {
            log.info("查询Spring Boot属性: {}", propertyName);
            return String.format("属性 %s 用于配置...", propertyName);
        }
        
        @Tool("执行简单的数学计算")
        public double calculate(String expression) {
            log.info("计算表达式: {}", expression);
            // 简单实现，实际应该使用表达式解析器
            return 42.0;
        }
    }
    
    interface JavaAssistant {
        String chat(String message);
    }
    
    public static void main(String[] args) {
        String apiKey = System.getenv("OPENAI_API_KEY");
        
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(GPT_4)
                .temperature(0.0)
                .build();
        
        JavaDocTools tools = new JavaDocTools();
        
        JavaAssistant assistant = AiServices.builder(JavaAssistant.class)
                .chatLanguageModel(model)
                .tools(tools)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
        
        // 测试工具调用
        String response1 = assistant.chat("帮我查询ArrayList类的文档");
        log.info("AI: {}", response1);
        
        String response2 = assistant.chat("Spring Boot中server.port属性是做什么的？");
        log.info("AI: {}", response2);
        
        String response3 = assistant.chat("计算100加200等于多少");
        log.info("AI: {}", response3);
    }
}
```

### 方式三：通过REST API调用Python LangGraph服务

创建Python Flask服务包装LangGraph：

`langgraph_service.py`

```python
from flask import Flask, request, jsonify
from flask_cors import CORS
from langgraph.graph import StateGraph, END
from langchain_openai import ChatOpenAI
from langchain_core.messages import HumanMessage
from typing import TypedDict
import os

app = Flask(__name__)
CORS(app)

# 定义状态
class AgentState(TypedDict):
    messages: list
    result: str

# 初始化LLM
llm = ChatOpenAI(model="gpt-4", temperature=0)

# 定义节点
def agent_node(state: AgentState) -> AgentState:
    messages = state["messages"]
    response = llm.invoke(messages)
    return {
        "messages": messages + [response],
        "result": response.content
    }

# 创建图
workflow = StateGraph(AgentState)
workflow.add_node("agent", agent_node)
workflow.set_entry_point("agent")
workflow.add_edge("agent", END)
graph = workflow.compile()

@app.route('/chat', methods=['POST'])
def chat():
    data = request.json
    message = data.get('message', '')
    
    initial_state = {
        "messages": [HumanMessage(content=message)],
        "result": ""
    }
    
    result = graph.invoke(initial_state)
    
    return jsonify({
        "response": result["result"],
        "success": True
    })

@app.route('/health', methods=['GET'])
def health():
    return jsonify({"status": "healthy"})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
```

安装依赖并运行：

```powershell
pip install flask flask-cors
python langgraph_service.py
```

Java客户端调用：`src/main/java/com/example/langgraph/LangGraphClient.java`

```java
package com.example.langgraph;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LangGraphClient {
    
    private final String baseUrl;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public LangGraphClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }
    
    public ChatResponse chat(String message) throws IOException {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("message", message);
        
        String jsonBody = objectMapper.writeValueAsString(requestBody);
        
        Request request = new Request.Builder()
                .url(baseUrl + "/chat")
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response);
            }
            
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, ChatResponse.class);
        }
    }
    
    @Data
    public static class ChatResponse {
        private String response;
        private boolean success;
    }
    
    public static void main(String[] args) {
        LangGraphClient client = new LangGraphClient("http://localhost:5000");
        
        try {
            ChatResponse response = client.chat("什么是Spring Boot？");
            System.out.println("AI回复: " + response.getResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

---

## 推荐学习路径

### 第一阶段：基础入门（1-2周）

#### Dify路径
1. ✅ 使用Docker安装Dify
2. ✅ 创建第一个聊天机器人
3. ✅ 学习提示词工程基础
4. ✅ 创建知识库并测试RAG
5. ✅ 通过Java调用Dify API

#### LangGraph路径
1. ✅ 安装Python和LangGraph环境
2. ✅ 理解状态图的概念
3. ✅ 创建简单的对话Agent
4. ✅ 学习工具调用机制
5. ✅ 通过Java调用LangGraph服务

### 第二阶段：进阶实践（2-4周）

#### Dify进阶
- 🔧 创建复杂的工作流
- 🔧 集成外部API和工具
- 🔧 优化知识库检索效果
- 🔧 实现多轮对话管理
- 🔧 部署到生产环境

#### LangGraph进阶
- 🔧 构建多Agent协作系统
- 🔧 实现状态持久化
- 🔧 添加人机协作功能
- 🔧 优化Agent决策逻辑
- 🔧 集成向量数据库

### 第三阶段：项目实战（4-8周）

#### 项目建议
1. **智能客服系统**
   - 使用Dify构建知识库
   - Java后端集成Dify API
   - 实现多轮对话和上下文管理

2. **代码审查助手**
   - 使用LangGraph构建多Agent系统
   - 研究员、编码员、审查员协作
   - Java集成并提供Web界面

3. **文档问答系统**
   - 上传技术文档到Dify
   - 实现语义搜索和问答
   - Java Spring Boot后端

4. **自动化工作流**
   - LangGraph构建复杂工作流
   - 集成多个外部工具
   - Java调度和监控

### 学习资源

#### 官方文档
- Dify文档：https://docs.dify.ai/
- LangGraph文档：https://langchain-ai.github.io/langgraph/
- LangChain4j文档：https://docs.langchain4j.dev/

#### 社区资源
- Dify GitHub：https://github.com/langgenius/dify
- LangGraph GitHub：https://github.com/langchain-ai/langgraph
- LangChain4j GitHub：https://github.com/langchain4j/langchain4j

#### 推荐课程
- LangChain官方教程
- OpenAI API文档
- Prompt Engineering指南

---

## 常见问题FAQ

### Q1: Dify和LangGraph应该选哪个？

**选择Dify如果：**
- 需要快速原型开发
- 团队成员不熟悉编程
- 主要做聊天机器人和RAG应用
- 需要可视化工作流编辑

**选择LangGraph如果：**
- 需要复杂的Agent系统
- 需要精细控制执行逻辑
- 团队有Python开发能力
- 需要高度定制化

**建议：** 两者结合使用，Dify用于快速验证想法，LangGraph用于复杂逻辑实现。

### Q2: 如何获取OpenAI API Key？

1. 访问：https://platform.openai.com/
2. 注册账号并登录
3. 进入API Keys页面
4. 创建新的API Key
5. 保存密钥（只显示一次）

**注意：** 需要绑定支付方式，按使用量付费。

### Q3: 国内如何访问OpenAI API？

**方案1：** 使用代理服务
- 配置HTTP代理
- 使用国内的OpenAI API代理服务

**方案2：** 使用国产大模型
- 阿里云通义千问
- 百度文心一言
- 智谱AI GLM
- 月之暗面Kimi

### Q4: Java项目如何管理API密钥？

**推荐做法：**

1. 使用环境变量
```java
String apiKey = System.getenv("OPENAI_API_KEY");
```

2. 使用配置文件（不提交到Git）
```properties
# application.properties
openai.api.key=${OPENAI_API_KEY}
```

3. 使用密钥管理服务
- AWS Secrets Manager
- Azure Key Vault
- HashiCorp Vault

### Q5: 如何降低API调用成本？

**优化策略：**
1. 使用更便宜的模型（GPT-3.5 vs GPT-4）
2. 减少token使用量
3. 实现缓存机制
4. 使用本地嵌入模型
5. 批量处理请求

### Q6: 遇到依赖冲突怎么办？

```powershell
# 查看依赖树
mvn dependency:tree

# 排除冲突的依赖
# 在pom.xml中使用<exclusions>

# 清理并重新构建
mvn clean install
```

### Q7: Docker启动Dify失败？

**常见问题：**
1. 端口被占用：修改docker-compose.yml中的端口
2. 内存不足：增加Docker Desktop的内存限制
3. WSL2问题：更新WSL2到最新版本

```powershell
# 查看日志
docker-compose logs -f

# 重启服务
docker-compose restart

# 完全重建
docker-compose down -v
docker-compose up -d --build
```

---

## 下一步行动

### 立即开始

1. **选择你的路径**
   - [ ] 快速验证想法 → 选择Dify
   - [ ] 深度定制开发 → 选择LangGraph
   - [ ] 两者都尝试 → 推荐！

2. **准备环境**
   - [ ] 安装Docker Desktop
   - [ ] 安装Python 3.10+
   - [ ] 配置Java开发环境
   - [ ] 获取OpenAI API Key

3. **动手实践**
   - [ ] 跑通第一个示例
   - [ ] 修改参数观察效果
   - [ ] 尝试集成到Java项目

4. **加入社区**
   - [ ] 关注GitHub仓库
   - [ ] 加入Discord/Slack社区
   - [ ] 阅读官方文档

### 持续学习

- 📚 每周学习一个新概念
- 💻 每周完成一个小项目
- 🤝 参与开源项目贡献
- 📝 记录学习笔记和心得

---

## 总结

作为Java开发工程师转型AI应用开发：

1. **Dify** 适合快速上手，可视化操作，适合原型验证
2. **LangGraph** 适合深度开发，灵活可控，适合复杂应用
3. **Java集成** 通过REST API或LangChain4j都很方便
4. **学习路径** 从简单到复杂，从理论到实践

**记住：** AI应用开发的核心是理解业务需求和用户体验，技术只是工具。先用Dify快速验证想法，再用LangGraph实现复杂逻辑，最后用Java构建生产级应用。

祝你学习顺利！🚀
