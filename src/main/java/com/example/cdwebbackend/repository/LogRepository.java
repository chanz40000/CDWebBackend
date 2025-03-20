package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
    LogEntity findOneById(long id);
}
