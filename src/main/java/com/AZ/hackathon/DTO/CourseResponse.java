package com.AZ.hackathon.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CourseResponse {
    private String id;
    private String title;
    private String description;
    private List<ModuleResponse> modules;
    private List<YouTubeVideoResponse> youtubeRecommendations;
}
