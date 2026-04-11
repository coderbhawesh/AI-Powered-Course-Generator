package com.AZ.hackathon.service;

import com.AZ.hackathon.DTO.YouTubeRecommendationResponse;
import com.AZ.hackathon.DTO.YouTubeVideoResponse;
import com.AZ.hackathon.entity.Course;
import com.AZ.hackathon.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class YouTubeService {

    private static final String YOUTUBE_SEARCH_URL =
            "https://www.googleapis.com/youtube/v3/search";

    private final RestTemplate restTemplate = new RestTemplate();
    private final CourseRepository courseRepository;

    @Value("${youtube.api.key:}")
    private String youtubeApiKey;

    public YouTubeRecommendationResponse getRecommendationsByCourseName(String courseName, int maxResults) {
        validateApiKey();
        String query = buildQuery(courseName);
        int safeMaxResults = normalizeMaxResults(maxResults);
        String url = UriComponentsBuilder.fromUriString(YOUTUBE_SEARCH_URL)
                .queryParam("part", "snippet")
                .queryParam("type", "video")
                .queryParam("maxResults", safeMaxResults)
                .queryParam("q", query)
                .queryParam("key", youtubeApiKey)
                .build()
                .toUriString();

        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<YouTubeVideoResponse> videos = extractVideos(response);

        return new YouTubeRecommendationResponse(courseName, videos);
    }

    public YouTubeRecommendationResponse getRecommendationsByCourseId(String courseId, int maxResults) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return getRecommendationsByCourseName(course.getTitle(), maxResults);
    }

    private void validateApiKey() {
        if (youtubeApiKey == null || youtubeApiKey.isBlank()) {
            throw new RuntimeException("YouTube API key is not configured");
        }
    }

    private String buildQuery(String courseName) {
        if (courseName == null || courseName.isBlank()) {
            throw new RuntimeException("Course name is required");
        }

        return courseName.trim() + " course tutorial";
    }

    private List<YouTubeVideoResponse> extractVideos(Map<String, Object> response) {
        List<YouTubeVideoResponse> videos = new ArrayList<>();
        if (response == null || !(response.get("items") instanceof List<?> items)) {
            return videos;
        }

        for (Object itemObj : items) {
            if (!(itemObj instanceof Map<?, ?> item)) {
                continue;
            }

            Map<?, ?> idNode = asMap(item.get("id"));
            Map<?, ?> snippet = asMap(item.get("snippet"));
            String videoId = asString(idNode.get("videoId"));

            if (videoId.isBlank()) {
                continue;
            }

            videos.add(new YouTubeVideoResponse(
                    videoId,
                    asString(snippet.get("title")),
                    asString(snippet.get("description")),
                    extractThumbnail(asMap(snippet.get("thumbnails"))),
                    "https://www.youtube.com/watch?v=" + videoId,
                    asString(snippet.get("channelTitle"))
            ));
        }

        return videos;
    }

    private String extractThumbnail(Map<?, ?> thumbnails) {
        Map<?, ?> high = asMap(thumbnails.get("high"));
        if (!high.isEmpty()) {
            return asString(high.get("url"));
        }

        Map<?, ?> medium = asMap(thumbnails.get("medium"));
        if (!medium.isEmpty()) {
            return asString(medium.get("url"));
        }

        Map<?, ?> defaultThumbnail = asMap(thumbnails.get("default"));
        if (!defaultThumbnail.isEmpty()) {
            return asString(defaultThumbnail.get("url"));
        }

        return "";
    }

    private Map<?, ?> asMap(Object value) {
        if (value instanceof Map<?, ?> map) {
            return map;
        }

        return Map.of();
    }

    private String asString(Object value) {
        return value == null ? "" : value.toString();
    }

    private int normalizeMaxResults(int maxResults) {
        if (maxResults < 1) {
            return 1;
        }
        return Math.min(maxResults, 10);
    }
}
