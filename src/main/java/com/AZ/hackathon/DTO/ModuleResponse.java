package com.AZ.hackathon.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ModuleResponse {
    private String id;
    private String title;
    private List<LessonResponse> lessons;
}
