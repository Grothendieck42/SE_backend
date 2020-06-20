package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.entities.CourseEntity;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends CrudRepository<CourseEntity, String> {
    List<CourseEntity> findByName(String name);

    List<CourseEntity> findByNameLike(String name);

    List<CourseEntity> findByIdLikeAndNameLike(String id, String name);
}

