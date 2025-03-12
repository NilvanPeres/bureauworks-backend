package com.example.bureauworks.core.service.Client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OpenAiClientService {

    private final OpenAiService openAiService;
    private static final String promptHeader = """
        Return only the locale of the current content, adhering to the following structure:
        First, the name of the language in capital letters.
        Next, the region code (e.g., "US").
        The format should resemble: "EN_US". 
        Let's start:\n  
    """;
    
    @Value("${openai.model:gpt-3.5-turbo}")
    private String model;


    public String generateChatCompletion(String prompt) {
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(List.of(new ChatMessage("user", promptHeader + prompt)))
                .build();

        ChatCompletionResult result = openAiService.createChatCompletion(request);
        
        if (result.getChoices() == null || result.getChoices().isEmpty()) {
            return "No response generated";
        }
        
        return result.getChoices().get(0).getMessage().getContent();
    }
    
}
