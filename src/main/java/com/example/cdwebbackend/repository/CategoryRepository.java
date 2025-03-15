package com.example.cdwebbackend.repository;


import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryRepository, Long> {
    CategoryRepository findOneByCode(String code);
}
