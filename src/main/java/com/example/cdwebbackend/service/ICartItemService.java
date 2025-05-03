package com.example.cdwebbackend.service;

import com.example.cdwebbackend.entity.CartItemEntity;

import java.util.List;

public interface ICartItemService {

    List<CartItemEntity> getCartItemsByIds(List<Long> cartItemIds, Long userId);

}
