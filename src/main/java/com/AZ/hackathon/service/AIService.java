package com.AZ.hackathon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIService {

    private final GeminiService geminiService;

    public String generateCourse(String topic) {

        String prompt = """
        Generate a course in JSON format:
        Topic: %s
        
        Output:
        {
          "title": "",
          "description": "",
          "modules": [
            {
              "title": "",
              "lessons": ["", ""]
            }
          ]
        }
        """.formatted(topic);

        // Call OpenAI / Gemini API

        return geminiService.generateResponse(prompt);

    }
}
