package com.victiny.dida;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/7/23 10:52
 */
@SpringBootTest
public class ZhiPuAiTest {

    @Resource
    private ClientV4 clientV4;

    @Test
    public void test(){
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), "作为一名营销专家，请为智谱开放平台创作一个吸引人的slogan");
        messages.add(chatMessage);
//        每个请求都会生成一个id
//        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());
//        能用同步就同步，异步增加了复杂度，除非性能满足不了要求
//        stream 流式，生成答案后立即返回，一个字一个字的返回，false，默认等所有答案都有了再返回
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
//                .requestId(requestId)
                .build();
        ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
        System.out.println("model output:" + invokeModelApiResp.getData().getChoices().get(0));
    }
}
