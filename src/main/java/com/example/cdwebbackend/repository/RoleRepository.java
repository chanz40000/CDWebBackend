package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    public Optional<RoleEntity> findOneById(long id);
}
