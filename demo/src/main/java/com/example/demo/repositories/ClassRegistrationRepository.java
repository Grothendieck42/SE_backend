package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.demo.entities.*;

import java.util.List;
import java.util.Optional;

public interface ClassRegistrationRepository extends JpaRepository<ClassRegistrationEntity, Integer> {
    boolean existsByCrid(String crid);

    boolean existsByStudentAndClazz_Course(UserEntity student, CourseEntity course);

    boolean existsByStudentAndClazz(UserEntity student, ClassEntity clazz);

    Integer countByClazz(ClassEntity clazz);

    Optional<ClassRegistrationEntity> findByCrid(String crid);

    List<ClassRegistrationEntity> findByStudent(UserEntity student);

    List<ClassRegistrationEntity> findByStudentAndClazz(UserEntity student, ClassEntity clazz);

    Optional<ClassRegistrationEntity> findByStudentAndClazz_Course(UserEntity student, CourseEntity course);
}
