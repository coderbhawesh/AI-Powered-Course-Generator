package com.AZ.hackathon.controller;

import com.AZ.hackathon.entity.Course;
import com.AZ.hackathon.repository.CourseRepository;
import com.AZ.hackathon.service.PDFService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PDFController {

    private final CourseRepository courseRepository;
    private final PDFService pdfService;

    @GetMapping("/courses/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable String id) {

        Course course = courseRepository.findById(id)
                .orElseThrow();

        byte[] pdf = pdfService.generatePdf(course);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=course.pdf")
                .header("Content-Type", "application/pdf")
                .body(pdf);
    }
}
