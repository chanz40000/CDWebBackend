package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    OrderEntity findOneById(long id);
    List<OrderEntity> findAllByCreatedDate(Date createDate);
     List<OrderEntity> findAllByCreatedDateBetween(Date start,Date end);
    List<OrderEntity> findByUserId(long userId);
    List<OrderEntity> findByStatusOrderId(long statusOrderId);
    OrderEntity findByUserIdAndId(long userId, long id);
    OrderEntity findByIdAndStatusOrderId(long id, long statusOrderId);
    List<OrderEntity> findByUserIdAndStatusOrderId(long userId, long status);
    Page<OrderEntity> findByStatusOrderId(Long statusId, Pageable pageable);

    // Phương thức tìm đơn hàng theo trạng thái và thời gian sửa đổi trước một ngày
    List<OrderEntity> findByStatusOrderIdAndModifiedDateBefore(long statusOrderId, Date date);
}