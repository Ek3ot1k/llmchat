package com.example.llmchat.client.dto;

public record ChatResponse(String model,
                           MessageDTO message,
                           boolean done) {
}
