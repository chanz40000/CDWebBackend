package com.example.cdwebbackend.responses;

import com.example.cdwebbackend.entity.ShippingAddressEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShippingAddressResponse {
    private Long id;
    private Long userId;
    private String receiverName;
    private String receiverPhone;
    private String province;
    private String district;
    private String ward;
    private String addressDetail;
    private boolean isDefault;

    public static ShippingAddressResponse fromEntity(ShippingAddressEntity entity) {
        return ShippingAddressResponse.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .receiverName(entity.getReceiverName())
                .receiverPhone(entity.getReceiverPhone())
                .province(entity.getProvince())
                .district(entity.getDistrict())
                .ward(entity.getWard())
                .addressDetail(entity.getAddressDetail())
                .isDefault(entity.isDefault())
                .build();
    }

}
