package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
    RatingEntity findOneById(long id);
}