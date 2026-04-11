package com.AZ.hackathon.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeVideoResponse {
    private String videoId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String videoUrl;
    private String channelTitle;
}
