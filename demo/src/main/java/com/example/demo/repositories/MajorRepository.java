package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.entities.MajorEntity;

import java.util.Optional;

public interface MajorRepository extends CrudRepository<MajorEntity, Short> {
    boolean existsByName(String name);

    Optional<MajorEntity> findByName(String name);
}
