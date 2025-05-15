package com.example.cdwebbackend.converter;

import com.example.cdwebbackend.dto.OrderDTO;
import com.example.cdwebbackend.dto.OrderDetailDTO;
import com.example.cdwebbackend.dto.ShippingAddressDTO;
import com.example.cdwebbackend.entity.*;
import com.example.cdwebbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderConverter {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShippingAddressRePository shippingAddressRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StatusOrderRepository statusOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSizeColorRepository productSizeColorRepository;

    @Autowired
    private OrderDetailConverter orderDetailConverter;



    public OrderEntity toEntity(OrderDTO dto) {
        OrderEntity entity = new OrderEntity();
        entity.setId(dto.getId());
        entity.setCreatedDate(dto.getCreateDate());
        entity.setTotalPrice(dto.getTotalPrice());
        entity.setFinalPrice(dto.getFinalPrice());
        entity.setNote(dto.getNote());
        entity.setReceiverName(dto.getReceiverName());
        entity.setReceiverPhone(dto.getReceiverPhone());
        entity.setDistrict(dto.getDistrict());
        entity.setProvince(dto.getProvince());
        entity.setWard(dto.getWard());
        entity.setAddressDetail(dto.getAddressDetail());


        if (dto.getPayment() != null) {
            entity.setPayment(paymentRepository.findOneById(dto.getPayment()));
        }

        if (dto.getStatusOrder() != null){
            entity.setStatusOrder(statusOrderRepository.findOneById(dto.getStatusOrder()));
        }

        if (dto.getUserId() != null) {
            UserEntity userEntity = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            entity.setUser(userEntity);

        }

//        if (entity.getUser() != null) {
//            dto.setUserId(entity.getUser().getId());
//        }


        List<OrderDetailDTO> orderDetailDTOList = dto.getOrderDetails();
        List<OrderDetailEntity> orderDetailEntityList = new ArrayList<>();
        if (orderDetailDTOList != null) {
            for (OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
                OrderDetailEntity orderDetailEntity = orderDetailConverter.toEntity(orderDetailDTO, entity);
                orderDetailEntityList.add(orderDetailEntity);
            }
            entity.setOrderDetails(orderDetailEntityList);
        }
        return entity;
    }

    public OrderDTO toDTO (OrderEntity entity) {
        OrderDTO dto = new OrderDTO();
        dto.setId(entity.getId());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setCreateDate(entity.getCreatedDate());
        dto.setFinalPrice(entity.getFinalPrice());
        dto.setNote(entity.getNote());
        dto.setReceiverName(entity.getReceiverName());
        dto.setReceiverPhone(entity.getReceiverPhone());
        dto.setDistrict(entity.getDistrict());
        dto.setProvince(entity.getProvince());
        dto.setWard(entity.getWard());
        dto.setAddressDetail(entity.getAddressDetail());
        if (entity.getPayment() != null) {
            dto.setPayment(entity.getPayment().getId());
        }
        if (entity.getStatusOrder() != null) {
            dto.setStatusOrder(entity.getStatusOrder().getId());
        }

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }
        List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
        List<OrderDetailEntity> orderDetailEntityList = entity.getOrderDetails();
        if (orderDetailEntityList != null) {
            for (OrderDetailEntity orderDetailEntity : orderDetailEntityList) {
                OrderDetailDTO orderDetailDTO = orderDetailConverter.toDTO(orderDetailEntity);
                orderDetailDTOList.add(orderDetailDTO);
            }
            dto.setOrderDetails(orderDetailDTOList);
        }
        return dto;
    }



}
