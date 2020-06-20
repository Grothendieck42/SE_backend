package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.entities.MajorClassEntity;

import java.util.Optional;

public interface MajorClassRepository extends CrudRepository<MajorClassEntity, Short> {
    boolean existsByName(String name);

    Optional<MajorClassEntity> findByName(String name);
}