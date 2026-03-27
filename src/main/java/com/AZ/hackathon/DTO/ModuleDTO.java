package com.AZ.hackathon.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ModuleDTO {

    private String title;

    // list of lesson titles (initial stage)
    private List<String> lessons;
}