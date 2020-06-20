package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.ClassEntity;
import com.example.demo.entities.SemesterEnum;

import java.util.List;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {

    List<ClassEntity> findByCourse_Name(String name);

    List<ClassEntity> findByCourse_NameLike(String name);

    List<ClassEntity> findByCourse_NameAndYearAndSemester(String name, Integer year, SemesterEnum semester);

    List<ClassEntity> findByCourse_NameLikeAndYearAndSemester(String name, Integer year, SemesterEnum semester);

    List<ClassEntity> findByCourse_Id(String Id);

    List<ClassEntity> findByCourse_IdAndYearAndSemester(String Id, Integer year, SemesterEnum semester);

    List<ClassEntity> findByCourseNameContainingAndYearAndSemester(String courseName, Integer year, SemesterEnum
            semester);

    List<ClassEntity> findByYearAndSemester(Integer year, SemesterEnum semester);

    List<ClassEntity> findByCourse_IdAndTeacher_NameLike(String cid, String name);

    List<ClassEntity> findByCourse_NameLikeAndTeacher_NameLike(String courseName, String teacherName);

    List<ClassEntity> findByTeacher_NameLike(String name);
}
