package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public Optional<UserEntity> findOneById(long id);
    public Optional<UserEntity> findOneByUsername(String username);
    @EntityGraph(attributePaths = "roles")
    Optional<UserEntity> findByUsername(String username);
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
    public Optional<UserEntity> findOneByGoogleAccountId(String googleAccountId);
    public Optional<UserEntity> findOneByFacebookAccountId(String facebookAccountId);

    UserEntity findOneByEmail(String email);
}

