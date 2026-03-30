package com.AZ.hackathon.service;

import com.AZ.hackathon.entity.Course;
import com.AZ.hackathon.entity.Lesson;
import com.AZ.hackathon.entity.Module;
import com.AZ.hackathon.repository.LessonRepository;
import com.AZ.hackathon.repository.ModuleRepository;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PDFService {

    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    public PDFService(ModuleRepository moduleRepository,
                      LessonRepository lessonRepository) {
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
    }

    public byte[] generatePdf(Course course) {
        
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph(course.getTitle())
                .setBold()
                .setFontSize(20));

        // Description
        if (course.getDescription() != null) {
            document.add(new Paragraph(course.getDescription())
                    .setMarginBottom(10));
        }

        //  Modules
        int moduleCount = 1;

        if (course.getModuleIds() != null) {
            for (String moduleId : course.getModuleIds()) {

                Module module = moduleRepository.findById(moduleId)
                        .orElse(null);

                if (module == null) continue;

                // Module Title
                document.add(new Paragraph(
                        moduleCount++ + ". " + module.getTitle())
                        .setBold()
                        .setFontSize(14)
                        .setMarginTop(10));

                // Lessons
                List lessonList = new List();

                int lessonCount = 1;

                if (module.getLessonIds() != null) {
                    for (String lessonId : module.getLessonIds()) {

                        Lesson lesson = lessonRepository.findById(lessonId)
                                .orElse(null);

                        if (lesson == null) continue;

                        lessonList.add(new ListItem(
                                lessonCount++ + ". " + lesson.getTitle()
                        ));
                    }
                }

                document.add(lessonList);
            }
        } else {
            // Fallback: fetch modules by courseId if moduleIds is null
            java.util.List<Module> modules = moduleRepository.findByCourseId(course.getId());
            for (Module module : modules) {
                // Module Title
                document.add(new Paragraph(
                        moduleCount++ + ". " + module.getTitle())
                        .setBold()
                        .setFontSize(14)
                        .setMarginTop(10));

                // Lessons
                List lessonList = new List();
                int lessonCount = 1;

                if (module.getLessonIds() != null) {
                    for (String lessonId : module.getLessonIds()) {
                        Lesson lesson = lessonRepository.findById(lessonId)
                                .orElse(null);

                        if (lesson == null) continue;

                        lessonList.add(new ListItem(
                                lessonCount++ + ". " + lesson.getTitle()
                        ));
                    }
                }

                document.add(lessonList);
            }
        }

        document.close();
        return out.toByteArray();
    }
}