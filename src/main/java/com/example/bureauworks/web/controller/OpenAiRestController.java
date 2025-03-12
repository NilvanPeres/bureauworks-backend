package com.example.bureauworks.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bureauworks.core.service.Client.OpenAiClientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/openai")
public class OpenAiRestController extends BaseRestController {

    private final OpenAiClientService openAiServiceClient;

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody String prompt) {
        return writeResponseBody(openAiServiceClient.generateChatCompletion(prompt));
    }
}
