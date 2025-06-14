package com.example.cdwebbackend.responses;

import com.example.cdwebbackend.entity.OrderEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
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
    private String codeCoupon;
    private int discountValue;
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
    private String orderReason;
    private List<OrderDetailResponse> orderDetails;
    private ShippingAddressResponse shippingAddresses;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date created;

    public static OrderResponse fromEntity(OrderEntity entity) {
        List<OrderDetailResponse> orderDetailResponses = entity.getOrderDetails().stream()
                .map(OrderDetailResponse::fromEntity)
                .collect(Collectors.toList());
//        ShippingAddressResponse shippingAddressResponse = null;
//        if (entity.getShippingAddress() != null) {
//            shippingAddressResponse = ShippingAddressResponse.fromEntity(entity.getShippingAddress());
//        }
        return OrderResponse.builder()
                .id(entity.getId())
                .created(entity.getCreatedDate())
                .userId(entity.getUser().getId())
                .statusOrderId(entity.getStatusOrder().getId())
                .totalPrice(entity.getTotalPrice())
                .shippingFee(entity.getShippingFee())
                .orderReason(entity.getCancelReason() != null ? entity.getCancelReason() : null)
                .finalPrice(entity.getFinalPrice())
                .discountValue(entity.getDiscountValue())
                .codeCoupon(entity.getCouponCode())
//                .statusOrderStatus(entity.getStatusOrder().getName())
                .paymentId(entity.getPayment().getId())
                .orderDetails(orderDetailResponses)
                .receiverName(entity.getReceiverName())
                .receiverPhone(entity.getReceiverPhone())
                .province(entity.getProvince())
                .district(entity.getDistrict())
                .ward(entity.getWard())
                .note(entity.getNote())
                .addressDetails(entity.getAddressDetail())
                .build();

    }

//    public static OrderResponse fromEntityPrepare(OrderEntity entity) {
//
//    }

}
