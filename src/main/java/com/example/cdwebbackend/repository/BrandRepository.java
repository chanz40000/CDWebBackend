package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Long> {
    public BrandEntity findOneById(long id);

}
