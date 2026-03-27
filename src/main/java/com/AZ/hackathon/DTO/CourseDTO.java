package com.AZ.hackathon.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CourseDTO {

    private String title;
    private String description;

    private List<String> tags;

    private List<ModuleDTO> modules;
}
