package com.Aura.utils;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@dev.langchain4j.service.spring.AiService(
    wiringMode = AiServiceWiringMode.EXPLICIT,
    streamingChatModel = "oa-stream",
    chatMemoryProvider = "cmp",
    contentRetriever = "contentRetriever",
    tools = {"userAiTools"}
)
public interface AiService {

    @SystemMessage("你是 Aura 酒店客服。用工具查酒店、房型、空房、下单、查订单，别编数据。\n" +
            "当前用户信息：{{userContext}}")
    Flux<String> userChat(@MemoryId String memoryId, @V("userContext") String userContext, @UserMessage String message);

    @SystemMessage("你是 Aura 酒店数据分析师，服务管理层。\n" +
            "1. 只能根据我给你的数据回答，别瞎编\n" +
            "2. 指出问题（比如退房率、取消率），给建议\n" +
            "3. 分点说清楚\n\n" +
            "数据如下：\n{{realData}}")
    Flux<String> adminChat(@MemoryId String memoryId, @V("realData") String realData, @UserMessage String message);
}
