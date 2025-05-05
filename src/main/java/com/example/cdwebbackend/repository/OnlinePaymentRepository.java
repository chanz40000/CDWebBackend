package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.OnlinePaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OnlinePaymentRepository extends JpaRepository<OnlinePaymentEntity, Long> {

    // Giả sử mã đơn hàng (txnRef) được lưu trong bảng OrderEntity với trường "code"
    Optional<OnlinePaymentEntity> findByOrderId(long id);
}
