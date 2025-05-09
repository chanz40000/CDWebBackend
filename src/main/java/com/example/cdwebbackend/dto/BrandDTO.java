package com.example.cdwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BrandDTO extends AbstractDTO<BrandDTO>{
    @JsonProperty("brand_id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
