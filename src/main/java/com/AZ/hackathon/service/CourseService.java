package com.AZ.hackathon.service;

import com.AZ.hackathon.DTO.*;
import com.AZ.hackathon.entity.Course;
import com.AZ.hackathon.entity.Lesson;
import com.AZ.hackathon.entity.Module;
import com.AZ.hackathon.repository.CourseRepository;
import com.AZ.hackathon.repository.LessonRepository;
import com.AZ.hackathon.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    private final AIService aiService;
    private final CourseRepository courseRepo;
    private final ModuleRepository moduleRepo;
    private final LessonRepository lessonRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public CourseResponse generateCourse(String topic) {

        // 1. Call AI → get course structure JSON
        String aiResponse = """
                {
          "title": "jehkee",
          "description": "hello",
          "modules": [
            {
              "title": "yowo",
              "lessons": ["yuyu", "uwuw"]
            }
          ]
        }
        """;
            //aiService.generateCourse(topic);


        log.info("AI Response: {}", aiResponse);
        // 2. Parse JSON → Java object
        CourseDTO dto = parse(aiResponse);

        // 3. Save Course
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());

        course = courseRepo.save(course);

        log.debug("checking course :{}", course);

        // 4. Save Modules + Lessons
        for (ModuleDTO m : dto.getModules()) {

            com.AZ.hackathon.entity.Module module = new Module();
            module.setTitle(m.getTitle());
            module.setCourseId(course.getId());

            module = moduleRepo.save(module);

            List<String> lessonIds = new ArrayList<>();
            for (String lessonTitle : m.getLessons()) {
                Lesson lesson = new Lesson();
                lesson.setTitle(lessonTitle);
                lesson.setModuleId(module.getId());

                lesson = lessonRepo.save(lesson);
                lessonIds.add(lesson.getId());
            }
            
            // Update module with lesson IDs
            module.setLessonIds(lessonIds);
            moduleRepo.save(module);
        }

        CourseResponse response = mapToResponse(course);

    }

    private CourseDTO parse(String aiResponse) {
        try {
            String cleanJson = extractJson(aiResponse);
            return objectMapper.readValue(cleanJson, CourseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid AI response", e);
        }
    }

    private String extractJson(String response) {
        if (response == null || response.isEmpty()) {
            throw new RuntimeException("Empty AI response");
        }

        int start = response.indexOf("{");
        int end = response.lastIndexOf("}");

        if (start == -1 || end == -1 || start > end) {
            throw new RuntimeException("No valid JSON found in response: " + response);
        }

        return response.substring(start, end + 1);
    }

    private CourseResponse mapToResponse(Course course) {

        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setTitle(course.getTitle());
        response.setDescription(course.getDescription());

        // fetch modules
        List<Module> modules = moduleRepo.findByCourseId(course.getId());

        List<ModuleResponse> moduleResponses = new ArrayList<>();

        for (Module module : modules) {

            ModuleResponse moduleResponse = new ModuleResponse();
            moduleResponse.setId(module.getId());
            moduleResponse.setTitle(module.getTitle());

            // fetch lessons
            List<Lesson> lessons = lessonRepo.findByModuleId(module.getId());

            List<LessonResponse> lessonResponses = new ArrayList<>();

            for (Lesson lesson : lessons) {
                LessonResponse lr = new LessonResponse();
                lr.setId(lesson.getId());
                lr.setTitle(lesson.getTitle());
                lessonResponses.add(lr);
            }

            moduleResponse.setLessons(lessonResponses);
            moduleResponses.add(moduleResponse);
        }

        response.setModules(moduleResponses);

        return response;
    }
}
