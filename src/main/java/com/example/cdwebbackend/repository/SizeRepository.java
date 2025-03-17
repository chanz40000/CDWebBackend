package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.SizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<SizeEntity, Long> {
    public SizeEntity findOneById(long id);

}
