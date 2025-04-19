package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.ImageEntity;
import com.example.cdwebbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository  extends JpaRepository<ImageEntity, Long> {



}
