package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    CommentEntity findOneById(long id);
}