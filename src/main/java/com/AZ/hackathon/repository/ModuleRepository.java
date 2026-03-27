package com.AZ.hackathon.repository;

import com.AZ.hackathon.entity.Module;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ModuleRepository extends MongoRepository<Module, String> {
    List<com.AZ.hackathon.entity.Module> findByCourseId(String courseId);
}