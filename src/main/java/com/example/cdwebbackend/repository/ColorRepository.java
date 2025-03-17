package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.ColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<ColorEntity, Long> {
    public ColorEntity findOneById(long id);
    public ColorEntity findOneByName(String name);
}
