package com.example.cdwebbackend.converter;

import com.example.cdwebbackend.dto.ShippingAddressDTO;
import com.example.cdwebbackend.entity.ShippingAddressEntity;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShippingAddressConverter {

    @Autowired
    private UserRepository userRepository;

    public ShippingAddressEntity toEntity(ShippingAddressDTO shippingAddressDTO) {
        ShippingAddressEntity shippingAddressEntity = new ShippingAddressEntity();
        shippingAddressEntity.setId(shippingAddressDTO.getId());
        shippingAddressEntity.setProvince(shippingAddressDTO.getProvince());
        shippingAddressEntity.setAddressDetail(shippingAddressDTO.getAddressDetail());
        shippingAddressEntity.setDistrict(shippingAddressDTO.getDistrict());
        shippingAddressEntity.setWard(shippingAddressDTO.getWard());
        shippingAddressEntity.setReceiverName(shippingAddressDTO.getReceiverName());
        shippingAddressEntity.setReceiverPhone(shippingAddressDTO.getReceiverPhone());
        shippingAddressEntity.setDefault(shippingAddressDTO.isDefault());

        if (shippingAddressEntity.getUser() != null) {
            UserEntity userEntity = userRepository.findById(shippingAddressDTO.getUser()).get();
            shippingAddressEntity.setUser(userEntity);
        }
        return shippingAddressEntity;
    }
    public ShippingAddressDTO toDto(ShippingAddressEntity shippingAddressEntity) {
        ShippingAddressDTO shippingAddressDTO = new ShippingAddressDTO();
        shippingAddressDTO.setId(shippingAddressEntity.getId());
        shippingAddressDTO.setProvince(shippingAddressEntity.getProvince());
        shippingAddressDTO.setAddressDetail(shippingAddressEntity.getAddressDetail());
        shippingAddressDTO.setDistrict(shippingAddressEntity.getDistrict());
        shippingAddressDTO.setWard(shippingAddressEntity.getWard());
        shippingAddressDTO.setReceiverName(shippingAddressEntity.getReceiverName());
        shippingAddressDTO.setReceiverPhone(shippingAddressEntity.getReceiverPhone());
        shippingAddressDTO.setDefault(shippingAddressEntity.isDefault());
        if (shippingAddressEntity.getUser() != null) {
            shippingAddressDTO.setUser(shippingAddressEntity.getUser().getId());
        }
        return shippingAddressDTO;
    }
}
