package com.example.cdwebbackend.responses;

import com.example.cdwebbackend.entity.OrderEntity;
import jakarta.persistence.criteria.Order;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class OrderResponse {
    private Long id;
    private Long userId;
    private int totalPrice;
    private int shippingFee;
    private int finalPrice;
    private Long statusOrderId;
    private String statusOrderStatus;
    private Long paymentId;
    private String receiverName;
    private String receiverPhone;
    private String province;
    private String district;
    private String ward;
    private String addressDetails;
    private String note;
    private List<OrderDetailResponse> orderDetails;
    private ShippingAddressResponse shippingAddresses;

    public static OrderResponse fromEntity(OrderEntity entity) {
        List<OrderDetailResponse> orderDetailResponses = entity.getOrderDetails().stream()
                .map(OrderDetailResponse::fromEntity)
                .collect(Collectors.toList());
        ShippingAddressResponse shippingAddressResponse = null;
        if (entity.getShippingAddress() != null) {
            shippingAddressResponse = ShippingAddressResponse.fromEntity(entity.getShippingAddress());
        }
        return OrderResponse.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .statusOrderId(entity.getStatusOrder().getId())
//                .statusOrderStatus(entity.getStatusOrder().getName())
                .paymentId(entity.getPayment().getId())
                .orderDetails(orderDetailResponses)
                .receiverName(entity.getShippingAddress().getReceiverName())
                .receiverPhone(entity.getShippingAddress().getReceiverPhone())
                .province(entity.getShippingAddress().getProvince())
                .district(entity.getShippingAddress().getDistrict())
                .ward(entity.getShippingAddress().getWard())
                .note(entity.getNote())
                .addressDetails(entity.getShippingAddress().getAddressDetail())
                .build();

    }

//    public static OrderResponse fromEntityPrepare(OrderEntity entity) {
//
//    }

}
