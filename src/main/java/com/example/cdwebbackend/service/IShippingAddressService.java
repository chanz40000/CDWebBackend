package com.example.cdwebbackend.service;

import com.example.cdwebbackend.dto.ShippingAddressDTO;
import com.example.cdwebbackend.entity.ShippingAddressEntity;
import com.example.cdwebbackend.exceptions.DataNotFoundException;

import java.util.List;

public interface IShippingAddressService {

//    public ShippingAddressDTO addShippingAddress(Long userId, String name, String phone, String province, String district, String ward, String addressDetail);
public ShippingAddressEntity addShippingAddress(Long userId, ShippingAddressDTO shippingAddressDTO);
    public List<ShippingAddressEntity> getAllShippingAddress(Long userId);
    public void chooseShippingAddress(Long userId, Long addressId);
    public ShippingAddressEntity getSelectedAddress(Long userId);
    public ShippingAddressEntity updateShippingAddress(Long userId, Long addressId, ShippingAddressDTO shippingAddressDTO) throws DataNotFoundException;
    public void deleteShippingAddress(Long userId, Long addressId) throws DataNotFoundException;
}
