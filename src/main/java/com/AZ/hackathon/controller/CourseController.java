package com.AZ.hackathon.controller;

import com.AZ.hackathon.DTO.CourseResponse;
import com.AZ.hackathon.DTO.GenerateCourseRequest;
import com.AZ.hackathon.service.CourseService;
import com.AZ.hackathon.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    private final GeminiService geminiService;

    @PostMapping("/generate")
    public ResponseEntity<CourseResponse> generateCourse(
            @RequestBody GenerateCourseRequest request) {

        return ResponseEntity.ok(
                courseService.generateCourse(request.getTopic())
        );
    }

    @PostMapping("/try")
    public ResponseEntity<String> check(
            @RequestBody GenerateCourseRequest request) {

        return ResponseEntity.ok(
                geminiService.generateResponse(request.getTopic())
        );
    }
}
