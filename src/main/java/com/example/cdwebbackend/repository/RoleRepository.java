package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    public RoleEntity findOneById(long id);
}
