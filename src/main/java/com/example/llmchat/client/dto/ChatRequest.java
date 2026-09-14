package com.example.llmchat.client.dto;

import java.util.List;

public record ChatRequest(String model,
                          List<MessageDTO> messages,
                          boolean stream) {
}
