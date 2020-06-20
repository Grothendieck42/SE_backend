package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.entities.DepartmentEntity;

import java.util.Optional;

public interface DepartmentRepository extends CrudRepository<DepartmentEntity, Short> {
    boolean existsByName(String name);

    Optional<DepartmentEntity> findByName(String name);

    void deleteByName(String name);
}

