package com.AZ.hackathon.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeRecommendationResponse {
    private String courseName;
    private List<YouTubeVideoResponse> recommendations;
}
