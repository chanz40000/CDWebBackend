package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.converter.OrderConverter;
import com.example.cdwebbackend.dto.OrderDTO;
import com.example.cdwebbackend.entity.OrderEntity;
import com.example.cdwebbackend.repository.OrderRepository;
import com.example.cdwebbackend.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderConverter orderConverter;

    @Override
    public List<OrderDTO> getAllOrdersByUserId(Long userId) {
        List<OrderDTO> orderDTOS = new ArrayList<>();

        List<OrderEntity> orderEntities = orderRepository.findByUserId(userId);
        for (OrderEntity orderEntity : orderEntities) {
            orderDTOS.add(orderConverter.toDTO(orderEntity));
        }
        return orderDTOS;
    }

    @Override
    public OrderDTO getOrdersByIdAndUserId(Long userId, Long orderId) {
        OrderEntity orderEntity = orderRepository.findByUserIdAndId(userId, orderId);
        return orderConverter.toDTO(orderEntity);
    }

    @Override
    public List<OrderDTO> getOrdersByUserIdAndStatusId(Long userId, Long statusId) {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        List<OrderEntity> orderEntities = orderRepository.findByUserIdAndStatusOrderId(userId, statusId);
        for (OrderEntity orderEntity : orderEntities) {
            orderDTOS.add(orderConverter.toDTO(orderEntity));
        }
        return orderDTOS;
    }

}
