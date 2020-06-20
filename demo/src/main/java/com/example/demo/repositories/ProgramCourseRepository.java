package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.entities.ProgramCourseEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.entities.CourseEntity;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ProgramCourseRepository extends CrudRepository<ProgramCourseEntity, Short> {
    boolean existsByCourse(CourseEntity course);

    boolean existsByStudent(UserEntity student);

    boolean existsByCourseAndStudent(CourseEntity course, UserEntity student);

    List<CourseEntity> findById(Integer pid);

    Optional<ProgramCourseEntity> findByCourseAndStudent(CourseEntity course, UserEntity student);

}

