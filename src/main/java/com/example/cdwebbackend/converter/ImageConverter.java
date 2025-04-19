package com.example.cdwebbackend.converter;

import com.example.cdwebbackend.dto.ImageDTO;
import com.example.cdwebbackend.entity.ImageEntity;
import com.example.cdwebbackend.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class ImageConverter {
    // converter từ DTO qua ENtity
    public ImageEntity toEntity(ImageDTO imageDTO) {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setName(imageDTO.getName());
        imageEntity.setType(imageDTO.getType());
        imageEntity.setUrl(imageDTO.getUrl());
        imageEntity.setSize(imageDTO.getSize());
        return imageEntity;
    }
}
