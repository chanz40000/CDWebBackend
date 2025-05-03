package com.example.cdwebbackend.converter;

import com.example.cdwebbackend.dto.OrderDetailDTO;
import com.example.cdwebbackend.entity.OrderDetailEntity;
import com.example.cdwebbackend.entity.OrderEntity;
import com.example.cdwebbackend.repository.OrderRepository;
import com.example.cdwebbackend.repository.ProductRepository;
import com.example.cdwebbackend.repository.ProductSizeColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailConverter {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSizeColorRepository productSizeColorRepository;

    @Autowired
    private OrderRepository orderRepository;

    public OrderDetailEntity toEntity(OrderDetailDTO orderDetailDTO, OrderEntity orderEntity) {
        OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
        orderDetailEntity.setId(orderDetailDTO.getId());
        orderDetailEntity.setQuantity(orderDetailDTO.getQuantity());
        orderDetailEntity.setPriceUnit(orderDetailDTO.getPriceUnit());
        orderDetailEntity.setSubtotal(orderDetailDTO.getSubtotal());

        orderDetailEntity.setOrder(orderEntity);

        if (orderDetailDTO.getProductId() != null){
            orderDetailEntity.setProduct(productRepository.findOneById(orderDetailDTO.getProductId()));
        }

        if (orderDetailDTO.getProductSizeColor() != null){
            orderDetailEntity.setProductSizeColor(productSizeColorRepository.findOneById(orderDetailDTO.getProductSizeColor()));
        }
        return orderDetailEntity;
    }
    public OrderDetailDTO toDTO(OrderDetailEntity orderDetailEntity) {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setId(orderDetailEntity.getId());
        orderDetailDTO.setQuantity(orderDetailEntity.getQuantity());
        orderDetailDTO.setPriceUnit(orderDetailEntity.getPriceUnit());
        orderDetailDTO.setSubtotal(orderDetailEntity.getSubtotal());
        if (orderDetailEntity.getProduct() != null){
            orderDetailDTO.setProductId(orderDetailEntity.getProduct().getId());
        }
        if (orderDetailEntity.getProductSizeColor() != null){
            orderDetailDTO.setProductSizeColor(orderDetailEntity.getProductSizeColor().getId());
        }
        if (orderDetailEntity.getOrder() != null){
            orderDetailDTO.setOrderId(orderDetailEntity.getOrder().getId());
        }
        return orderDetailDTO;

    }
}
