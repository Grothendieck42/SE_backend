package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.entities.AuthorityEntity;

import java.util.Optional;

public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Short> {
    Optional<AuthorityEntity> findByUri(String uri);

    boolean existsByUri(String uri);
}