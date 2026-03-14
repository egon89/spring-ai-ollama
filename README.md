# Spring AI Ollama
This project is a Spring Boot application that integrates with Ollama, an AI platform. 

Spring AI provides two primary APIs for AI chat interactions: **Chat Model API** (low-level) and **Chat Client API** (high-level). Chat Client builds on Chat Model for easier use in production apps.

## 🧠 Chat Model API
**Purpose:**  
Low-level abstraction for directly invoking an AI model.
- Direct access via `ChatModel.call(Prompt)` or `stream(Prompt)`
- Handles raw `Prompt` → `ChatResponse`
- No built-in Advisors, ChatMemory, or orchestration

### Memory Usage with ChatModel
You can use `ChatMemory`, but you must manage it manually:
- Retrieve conversation messages from memory
- Build a `Prompt` including those messages
- Call `chatModel.call(prompt)`
- Store the model response back into memory

👉 Memory handling is fully controlled by your application logic.

## 💬 Chat Client API
**Purpose:**  
High-level fluent API built on top of `ChatModel`
- Fluent builder: `.user("text").system("role").call()`
- Supports streaming, multi-turn conversations, and response formatting
- Integrates advisors (e.g., RAG, logging) and Chat Memory automatically
- Designed for conversational applications

### Memory Usage with ChatClient
ChatMemory is typically used via Advisors:
- `MessageChatMemoryAdvisor`
- `PromptChatMemoryAdvisor`
- `VectorStoreChatMemoryAdvisor`

Memory is automatically:
- Retrieved before the call
- Injected into the prompt
- Updated after the response

Spring AI autoconfigures `MessageWindowChatMemory` as the default Chat Memory implementation for Chat Client API.
The `MessageWindowChatMemory` is autoconfigured with `InMemoryChatMemoryRepository`.

Key Features:
- **Window Size**: Max 20 messages (default, sliding window)
- **Behavior**: Keeps system messages, drops oldest when full
- **Storage**: `ConcurrentHashMap` by conversation ID
- **Advisor**: `MessageChatMemoryAdvisor` (auto-included in `ChatClient.defaultAdvisors()`)

### 🧩 Advisors API

Advisors act like an interception pipeline:

- Modify prompts before execution
- Modify responses after execution
- Inject memory
- Implement RAG
- Add logging, metrics, etc.

## Key Differences
| Aspect          | Chat Model API              | Chat Client API                  |
|-----------------|-----------------------------|----------------------------------|
| Abstraction    | Low-level, explicit prompts | High-level, fluent messages     |
| Advisors/Memory| Manual implementation      | Native via `defaultAdvisors()`  |
| Best For       | Minimal integrations       | Full apps with state/orchestration |


### Advisors & Memory Compatibility
- **Native**: Only with Chat Client (e.g., `MessageChatMemoryAdvisor`).
- **Chat Model**: Manual—fetch/add to `ChatMemory`, build prompts yourself.

## Tool Calling
The `returnDirect=true` flag indicates that the output of this tool should be returned **directly to the user**, 
rather than being processed further by the chat model. This is useful for tools that provide final answers or 
results that don't require additional context or formatting.
```java
@Tool(description = "Trade stocks.Example usage: 'Buy 100 shares of AAPL at market price'", returnDirect = true)
String tradeStocks(
    @ToolParam(description = "The stock symbol, e.g. AAPL") String stockSymbol,
    @ToolParam(description = "The number of shares to trade") int quantity,
    @ToolParam(description = "The type of trade, e.g. 'buy' or 'sell'") String tradeType) {}
```

## Ollama
To run Ollama with Docker, you can use this repository: [egon89/docker-mono-repo](https://github.com/egon89/docker-mono-repo/tree/main/ollama)
