package com.AZ.hackathon.controller;

import com.AZ.hackathon.DTO.CourseResponse;
import com.AZ.hackathon.DTO.GenerateCourseRequest;
import com.AZ.hackathon.DTO.YouTubeRecommendationResponse;
import com.AZ.hackathon.service.CourseService;
import com.AZ.hackathon.service.GeminiService;
import com.AZ.hackathon.service.YouTubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    private final GeminiService geminiService;
    private final YouTubeService youTubeService;

    @PostMapping("/generate")
    public ResponseEntity<CourseResponse> generateCourse(
            @RequestBody GenerateCourseRequest request) {

        return ResponseEntity.ok(
                courseService.generateCourse(request.getTopic())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourse(@PathVariable String id) {
        CourseResponse courseResponse = courseService.getCourse(id);
        return ResponseEntity.ok(courseResponse);
    }

    @GetMapping("/youtube-recommendations")
    public ResponseEntity<YouTubeRecommendationResponse> getYouTubeRecommendations(
            @RequestParam String courseName,
            @RequestParam(defaultValue = "5") int maxResults) {

        return ResponseEntity.ok(
                youTubeService.getRecommendationsByCourseName(courseName, maxResults)
        );
    }

    @GetMapping("/{id}/youtube-recommendations")
    public ResponseEntity<YouTubeRecommendationResponse> getCourseYouTubeRecommendations(
            @PathVariable String id,
            @RequestParam(defaultValue = "5") int maxResults) {

        return ResponseEntity.ok(
                youTubeService.getRecommendationsByCourseId(id, maxResults)
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
