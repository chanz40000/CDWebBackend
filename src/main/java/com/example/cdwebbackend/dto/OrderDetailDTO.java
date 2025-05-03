package com.example.cdwebbackend.dto;

import com.example.cdwebbackend.entity.ProductSizeColorEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderDetailDTO extends AbstractDTO<OrderDetailDTO> {
    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("product_size_color_id")
    private Long productSizeColor;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("price_unit")
    private int priceUnit;

    @JsonProperty("subtotal")
    private int subtotal;

    // Getters and Setters

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductSizeColor() {
        return productSizeColor;
    }

    public void setProductSizeColor(Long productSizeColor) {
        this.productSizeColor = productSizeColor;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(int priceUnit) {
        this.priceUnit = priceUnit;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }
}
