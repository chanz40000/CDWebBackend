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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
public class OnlinePaymentService implements IOnlinePaymentService {

    @Autowired
    private final OnlinePaymentRepository onlinePaymentRepository;
    @Autowired
    private final StatusOrderRepository statusOrderRepository;
    @Autowired
    private final OrderRepository orderRepository;

    @Override
    public void updatePaymentStatus(String id, Long id_status) {
        Optional<OrderEntity> order = orderRepository.findById(Long.parseLong(id));
        OrderEntity order1 = order.get();
        StatusOrderEntity statusOrder = statusOrderRepository.findOneById(id_status);
        order1.setStatusOrder(statusOrder);
        orderRepository.save(order1);
    }
}

