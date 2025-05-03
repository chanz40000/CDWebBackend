package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.entity.CartItemEntity;
import com.example.cdwebbackend.repository.CartItemRepository;
import com.example.cdwebbackend.service.ICartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService  implements ICartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public List<CartItemEntity> getCartItemsByIds(List<Long> cartItemIds, Long userId) {
        return cartItemRepository.findByIdInAndCart_User_Id(cartItemIds, userId);
    }
}
