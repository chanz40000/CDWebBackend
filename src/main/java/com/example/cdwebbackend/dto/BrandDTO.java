package com.example.cdwebbackend.dto;

public class BrandDTO extends AbstractDTO<BrandDTO>{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
