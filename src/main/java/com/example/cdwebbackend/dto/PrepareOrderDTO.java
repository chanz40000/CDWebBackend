package com.example.cdwebbackend.dto;

import com.example.cdwebbackend.responses.CartItemResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PrepareOrderDTO extends AbstractDTO<PrepareOrderDTO> {

    @JsonProperty("total_price")
    private int totalPrice;

    @JsonProperty("total_quantity")
    private int totalQuantity;

    @JsonProperty("total_quantity_product")
    private int totalQuantityProduct;

    @JsonProperty("cart_items_choose")
    private List<CartItemResponse> cartItems;

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getTotalQuantityProduct() {
        return totalQuantityProduct;
    }

    public void setTotalQuantityProduct(int totalQuantityProduct) {
        this.totalQuantityProduct = totalQuantityProduct;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<CartItemResponse> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemResponse> cartItems) {
        this.cartItems = cartItems;
    }
}
