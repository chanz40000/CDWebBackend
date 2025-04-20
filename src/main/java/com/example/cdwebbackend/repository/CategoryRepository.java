package com.example.cdwebbackend.repository;


import com.example.cdwebbackend.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    public CategoryEntity findOneById(long id);
}
