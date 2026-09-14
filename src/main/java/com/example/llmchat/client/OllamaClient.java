package com.example.llmchat.client;

import com.example.llmchat.client.dto.ChatRequest;
import com.example.llmchat.client.dto.ChatResponse;
import com.example.llmchat.client.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class OllamaClient {
    private final RestClient restClient;
    private final String model;

    public OllamaClient(
            @Value("${llm.base-url}") String baseUrl,
            @Value("${llm.model}") String model) {
        this.model = model;
        this.restClient= RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public String sendMessage(List<MessageDTO> history){
        ChatRequest request=new ChatRequest(model,history,false);

        ChatResponse response=restClient.post()
                .uri("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(ChatResponse.class);

        if (response==null || response.message()==null){
            throw new RuntimeException("Пустой ответ от LLM-сервера");
        }

        return response.message().content();
    }
}
