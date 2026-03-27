package com.AZ.hackathon.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "modules")
@Data
public class Module {
    @Id
    private String id;

    private String title;
    private String courseId;

    private List<String> lessonIds;
}