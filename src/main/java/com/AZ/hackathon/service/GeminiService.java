package com.AZ.hackathon.service;

import com.AZ.hackathon.DTO.GeminiRequest;
import com.AZ.hackathon.DTO.GeminiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini" + (char)45 + "2.5" + (char)45 + "flash:generateContent?key=";
    public String generateResponse(String prompt) {

        // Build request body
        GeminiRequest.Part part = new GeminiRequest.Part();
        part.setText(prompt);

        GeminiRequest.Content content = new GeminiRequest.Content();
        content.setParts(List.of(part));

        GeminiRequest request = new GeminiRequest();
        request.setContents(List.of(content));

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);

        // Call API
        ResponseEntity<GeminiResponse> response = restTemplate.exchange(
                URL + apiKey,
                HttpMethod.POST,
                entity,
                GeminiResponse.class
        );

        // Extract response safely
        if (response.getBody() != null &&
                response.getBody().getCandidates() != null &&
                !response.getBody().getCandidates().isEmpty()) {

            return response.getBody()
                    .getCandidates()
                    .get(0)
                    .getContent()
                    .getParts()
                    .get(0)
                    .getText();
        }

        return "No response from Gemini";
    }
}
