package com.example.llmchat;

import com.example.llmchat.client.OllamaClient;
import com.example.llmchat.client.dto.MessageDTO;
import com.example.llmchat.service.ChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
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
        assertEquals("Привет, Алексей", history.get(2).content());
    }

    @Test
    void shouldSendPreviousMessagesOnNextRequest(){
        List<List<MessageDTO>> requests=new ArrayList<>();

        when(ollamaClient.sendMessage(anyList())).thenAnswer(invocation->{
                List<MessageDTO> requestHistory=invocation.getArgument(0);
                requests.add(List.copyOf(requestHistory));
                return "Ответ: "+requests.size();
        });

        chatService.sendUserMessage("Меня зовут Алексей");
        chatService.sendUserMessage("Как меня зовут?");

        assertEquals(2,requests.size());

        assertIterableEquals(List.of(
                new MessageDTO("system",ChatService.SYSTEM_PROMPT),
                new MessageDTO("user","Меня зовут Алексей"),
                new MessageDTO("assistant","Ответ: 1"),
                new MessageDTO("user","Как меня зовут?")
        ),requests.get(1));
    }

    @Test
    void shouldClearHistoryAndKeepSystemPrompt(){
        when(ollamaClient.sendMessage(anyList())).thenReturn("Привет!");

        chatService.sendUserMessage("Меня зовут Алексей");
        chatService.resetChat();

        assertIterableEquals(List.of(
                new MessageDTO("system",ChatService.SYSTEM_PROMPT)
        ),chatService.getHistory());
    }
}
