package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, String> {
    /**
     * @param name
     * @return boolean
     * check exists of a UID
     */
    List<UserEntity> findByName(String name);

    Optional<UserEntity> findByUid(String uid);

    List<UserEntity> findByUidLikeAndNameLike(String uid, String name);
}
