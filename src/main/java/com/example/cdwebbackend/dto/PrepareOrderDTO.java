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

    @JsonProperty("discount_value")
    private int discountVallue;

    @JsonProperty("shipping_fee")
    private int shippingFee;

    @JsonProperty("final_price")
    private int finalPrice;

    @JsonProperty("coupon_code")
    private String couponCode;


    public int getDiscountVallue() {
        return discountVallue;
    }

    public void setDiscountVallue(int discountVallue) {
        this.discountVallue = discountVallue;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(int shippingFee) {
        this.shippingFee = shippingFee;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

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
