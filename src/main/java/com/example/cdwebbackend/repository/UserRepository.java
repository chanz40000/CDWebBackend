package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.dto.UserDTO;
import com.example.cdwebbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public UserEntity findOneById(long id);
    public Optional<UserEntity> findOneByUsername(String username);

}
