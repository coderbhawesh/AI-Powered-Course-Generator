package com.AZ.hackathon.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "courses")
@Data
public class Course {
    @Id
    private String id;

    private String title;
    private String description;
    private String creator; // userId

    private List<String> moduleIds;
    private List<String> tags;

    private LocalDateTime createdAt;
}
