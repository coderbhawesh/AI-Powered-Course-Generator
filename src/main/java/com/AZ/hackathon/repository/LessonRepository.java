package com.AZ.hackathon.repository;

import com.AZ.hackathon.entity.Lesson;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LessonRepository extends MongoRepository<Lesson, String> {
    List<Lesson> findByModuleId(String moduleId);
}
