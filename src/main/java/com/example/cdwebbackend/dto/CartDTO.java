package com.example.cdwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CartDTO extends AbstractDTO<CartDTO> {

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItems;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<CartItemDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDTO> cartItems) {
        this.cartItems = cartItems;
    }
}
