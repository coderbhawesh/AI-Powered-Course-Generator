package com.AZ.hackathon.repository;

import com.AZ.hackathon.entity.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepository extends MongoRepository<Course, String> {

}
