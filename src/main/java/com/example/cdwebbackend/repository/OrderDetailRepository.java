package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
    OrderDetailEntity findOneById(long id);
    @Query(value = "SELECT p.id, p.name_product, SUM(od.quantity) as totalQuantity " +
            "FROM order_detail od " +
            "JOIN product p ON od.product_id = p.id " +
            "GROUP BY p.id, p.name_product " +
            "ORDER BY totalQuantity DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> findTop10BestSellingProducts();

}