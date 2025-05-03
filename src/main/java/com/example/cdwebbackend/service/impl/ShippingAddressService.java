package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.converter.ShippingAddressConverter;
import com.example.cdwebbackend.dto.ShippingAddressDTO;
import com.example.cdwebbackend.entity.ShippingAddressEntity;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.repository.ShippingAddressRePository;
import com.example.cdwebbackend.repository.UserRepository;
import com.example.cdwebbackend.service.IShippingAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingAddressService implements IShippingAddressService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShippingAddressRePository shippingAddressRePository;

    @Autowired
    private ShippingAddressConverter shippingAddressConverter;


    @Override
    public ShippingAddressEntity addShippingAddress(Long userId, ShippingAddressDTO shippingAddressDTO) {
        boolean isDefault = false;
        ShippingAddressEntity shippingAddressEntity = shippingAddressConverter.toEntity(shippingAddressDTO);
        shippingAddressEntity.setDefault(isDefault);
        shippingAddressEntity.setUser(userRepository.findById(userId).get());
        // Lưu địa chỉ giao hàng vào cơ sở dữ liệu
        ShippingAddressEntity savedAddress = shippingAddressRePository.save(shippingAddressEntity);

        // Áp dụng phương thức chooseShippingAddress để thiết lập địa chỉ mặc định sau khi lưu
        chooseShippingAddress(userId, savedAddress.getId());

        return savedAddress;
    }
    @Override
    public List<ShippingAddressEntity> getAllShippingAddress(Long userId) {
        return shippingAddressRePository.findAllByUserId(userId);
    }

    @Override
    public void chooseShippingAddress(Long userId, Long addressId) {
        // Lấy toàn bộ địa chỉ giao hàng của user
        List<ShippingAddressEntity> addressEntities = shippingAddressRePository.findAllByUserId(userId);

        for (ShippingAddressEntity addressEntity : addressEntities) {
            // Nếu id của address trùng với id được chọn => true => đặt làm mặc định
            addressEntity.setDefault(addressEntity.getId().equals(addressId));
            shippingAddressRePository.save(addressEntity);
        }
    }

    @Override
    public ShippingAddressEntity getSelectedAddress(Long userId) {
        return shippingAddressRePository.findByUserIdAndIsDefaultTrue(userId)
                .orElse(null);
    }
}
