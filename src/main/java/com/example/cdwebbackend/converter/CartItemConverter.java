package com.example.cdwebbackend.converter;

import com.example.cdwebbackend.dto.CartItemDTO;
import com.example.cdwebbackend.entity.CartEntity;
import com.example.cdwebbackend.entity.CartItemEntity;
import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.entity.ProductSizeColorEntity;
import com.example.cdwebbackend.repository.ProductRepository;
import com.example.cdwebbackend.repository.ProductSizeColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartItemConverter {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSizeColorRepository productSizeColorRepository;

    public CartItemEntity toEntity(CartItemDTO cartItemDTO, CartEntity  cartEntity) {
        CartItemEntity cartItemEntity = new CartItemEntity();
        cartItemEntity.setId(cartItemDTO.getId());
        cartItemEntity.setQuantity(cartItemDTO.getQuantity());

        if (cartItemDTO.getProductId() != 0){
            ProductEntity productEntity = productRepository.findOneById(cartItemDTO.getProductId());
            cartItemEntity.setProduct(productEntity);
        }

        if (cartItemDTO.getProductSizeColorId() != 0){
            ProductSizeColorEntity productSizeColorEntity = productSizeColorRepository.findOneById(cartItemDTO.getProductSizeColorId());
            cartItemEntity.setProductSizeColor(productSizeColorEntity);
        }
        cartItemEntity.setCart(cartEntity);
        return cartItemEntity;
    }

    public CartItemDTO toDTO(CartItemEntity cartItemEntity) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setId(cartItemEntity.getId());
        cartItemDTO.setQuantity(cartItemEntity.getQuantity());
        if (cartItemEntity.getProduct() != null){
            cartItemDTO.setProductId(cartItemEntity.getProduct().getId());
        }
        if (cartItemEntity.getProductSizeColor() != null){
            cartItemDTO.setProductSizeColorId(cartItemEntity.getProductSizeColor().getId());
        }
        return cartItemDTO;
    }

}
