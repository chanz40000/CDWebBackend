package com.example.cdwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ProductColorDTO extends AbstractDTO<ProductColorDTO>{

    @JsonProperty("productId")
    private Long productId;

    @JsonProperty("colorId")
    private Long colorId;


    @JsonProperty("color")
    private String color;

    @JsonProperty("image")
    private String image;

    public ProductColorDTO() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long colorId) {
        this.colorId = colorId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
