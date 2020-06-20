package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.entities.SessionEntity;

import java.util.Optional;

public interface SqlSessionRepository extends CrudRepository<SessionEntity, Long> {
    boolean existsByUid(String uid);

    Optional<SessionEntity> findByUid(String uid);

    boolean existsByToken(String token);

    Optional<SessionEntity> findByToken(String token);
}
