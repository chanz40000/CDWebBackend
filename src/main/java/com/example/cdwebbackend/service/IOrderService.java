package com.example.cdwebbackend.service;

import com.example.cdwebbackend.dto.OrderDTO;

import java.util.List;

public interface IOrderService {
    public List<OrderDTO> getAllOrdersByUserId(Long userId);
    public OrderDTO getOrdersByIdAndUserId(Long userId, Long orderId);
    public List<OrderDTO> getOrdersByUserIdAndStatusId(Long userId, Long statusId);



}
