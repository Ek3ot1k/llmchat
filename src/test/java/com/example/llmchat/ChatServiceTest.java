package com.example.llmchat;

import com.example.llmchat.client.OllamaClient;
import com.example.llmchat.client.dto.MessageDTO;
import com.example.llmchat.service.ChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {
    @Mock
    private OllamaClient ollamaClient;

    @InjectMocks
    private ChatService chatService;

    @Test
    void shouldSaveHistoryAndIncludeSystemPrompt(){
        when(ollamaClient.sendMessage(anyList())).thenReturn("Привет, Алексей");

        chatService.sendUserMessage("Меня зовут Алексей");

        List<MessageDTO> history=chatService.getHistory();
        assertEquals(3,history.size());
        assertEquals("system",history.get(0).role());
        assertEquals("user",history.get(1).role());
        assertEquals("assistant",history.get(2).role());
    }
}
