package com.AZ.hackathon.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {

    private String title;
    private String description;

    private List<String> tags;

    private List<ModuleDTO> modules;
}
