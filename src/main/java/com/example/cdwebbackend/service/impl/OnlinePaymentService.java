package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.entity.OnlinePaymentEntity;
import com.example.cdwebbackend.entity.OrderEntity;
import com.example.cdwebbackend.entity.StatusOrderEntity;
import com.example.cdwebbackend.repository.OnlinePaymentRepository;
import com.example.cdwebbackend.repository.OrderRepository;
import com.example.cdwebbackend.repository.StatusOrderRepository;
import com.example.cdwebbackend.service.IOnlinePaymentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OnlinePaymentService implements IOnlinePaymentService {

    @Getter
    private final OnlinePaymentRepository onlinePaymentRepository;
    @Getter
    private final StatusOrderRepository statusOrderRepository;
    @Getter
    private final OrderRepository orderRepository;

    public OnlinePaymentService(OnlinePaymentRepository onlinePaymentRepository, StatusOrderRepository statusOrderRepository, OrderRepository orderRepository) {
        this.onlinePaymentRepository = onlinePaymentRepository;
        this.statusOrderRepository = statusOrderRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    @CachePut(value = "orders", key = "#id", condition = "#id != null && #id_status != null")
    public void updatePaymentStatus(String id, Long id_status) {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(Long.parseLong(id));
        if (optionalOrder.isEmpty()) {
            throw new RuntimeException("Order not found with id: " + id);
        }

        OrderEntity order = optionalOrder.get();

        StatusOrderEntity statusOrder = statusOrderRepository.findOneById(id_status);
        if (statusOrder == null) {
            throw new RuntimeException("StatusOrder not found with id: " + id_status);
        }

        order.setStatusOrder(statusOrder);
        orderRepository.save(order);
    }
}