package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.entities.SelectionTimeEntity;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface SelectionTimeRepository extends CrudRepository<SelectionTimeEntity, Integer> {
    Optional<SelectionTimeEntity> findByStartAndEnd(Timestamp start, Timestamp end);

    //@SuppressWarnings("AliMissingOverrideAnnotation")
    List<SelectionTimeEntity> findAll();

    Optional<SelectionTimeEntity> findById(Long id);
    //List<SelectionTimeEntity> findBy

    boolean existsByStartLessThanEqualAndEndGreaterThanEqualAndRegisterTrue(Timestamp start, Timestamp end);

    boolean existsByStartLessThanEqualAndEndGreaterThanEqualAndDropTrue(Timestamp start, Timestamp end);

    boolean existsByStartLessThanEqualAndEndGreaterThanEqualAndComplementTrue(Timestamp start, Timestamp end);

    boolean existsById(Long id);
}
