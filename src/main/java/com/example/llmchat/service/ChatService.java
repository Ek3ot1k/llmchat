package com.example.llmchat.service;

import com.example.llmchat.client.OllamaClient;
import com.example.llmchat.client.dto.MessageDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChatService {
    private final OllamaClient ollamaClient;
    private final List<MessageDTO> history=new ArrayList<>();

    public static final String SYSTEM_PROMPT=
            "Ты полезный технический ассистент. Отвечай кратко и понятно.";

    public ChatService(OllamaClient ollamaClient) {
        this.ollamaClient = ollamaClient;
        resetChat();
    }

    public String sendUserMessage(String userText){
        MessageDTO userMessage=new MessageDTO("user",userText);
        history.add(userMessage);

        try {
            String assistantResponse = ollamaClient.sendMessage(history);

            history.add(new MessageDTO("assistant", assistantResponse));

            return assistantResponse;
        }catch (RuntimeException exception){
            history.remove(history.size()-1);
            throw exception;
        }
    }

    public void resetChat(){
        history.clear();
        history.add(new MessageDTO("system",SYSTEM_PROMPT));
    }

    public List<MessageDTO> getHistory(){
        return Collections.unmodifiableList(history);
    }
}
