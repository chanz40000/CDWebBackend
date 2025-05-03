package com.example.cdwebbackend.converter;

import com.example.cdwebbackend.dto.CartDTO;
import com.example.cdwebbackend.dto.CartItemDTO;
import com.example.cdwebbackend.entity.CartEntity;
import com.example.cdwebbackend.entity.CartItemEntity;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.repository.CartItemRepository;
import com.example.cdwebbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartConverter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartItemConverter cartItemConverter;

    public CartEntity toEntity(CartDTO cartDTO) {
        CartEntity cartEntity = new CartEntity();
        cartEntity.setId(cartDTO.getId());

        if (cartDTO.getUserId() != 0){
            UserEntity userEntity = userRepository.findById(cartDTO.getUserId()).get();
            cartEntity.setUser(userEntity);
        }

        List<CartItemEntity> itemEntities = new ArrayList<>();
        if (cartDTO.getCartItems() != null){
            for (CartItemDTO cartItemDTO : cartDTO.getCartItems()) {
                CartItemEntity cartItemEntity = cartItemConverter.toEntity(cartItemDTO, cartEntity);
                itemEntities.add(cartItemEntity);
            }
        }
        cartEntity.setCartItems(itemEntities);
        return cartEntity;

    }

    public CartDTO toDTO(CartEntity cartEntity) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cartEntity.getId());

        if (cartEntity.getUser() != null){
            cartDTO.setUserId(cartEntity.getUser().getId());
        }
        List<CartItemDTO> cartItemDTOS = new ArrayList<>();
        if (cartEntity.getCartItems() != null){
            for (CartItemEntity cartItemEntity : cartEntity.getCartItems()) {
                CartItemDTO cartItemDTO = cartItemConverter.toDTO(cartItemEntity);
                cartItemDTOS.add(cartItemDTO);
            }
        }
        cartDTO.setCartItems(cartItemDTOS);
        return cartDTO;
    }

}
