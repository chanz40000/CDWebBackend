package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
//    public List<ProductEntity> findAll();
    public ProductEntity findOneById(long id);

    public List<ProductEntity> findAllByNameProductContainingIgnoreCase(String nameProduct);
}
