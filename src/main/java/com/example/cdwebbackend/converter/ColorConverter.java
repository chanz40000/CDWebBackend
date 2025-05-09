package com.example.cdwebbackend.converter;

import com.example.cdwebbackend.dto.CartDTO;
import com.example.cdwebbackend.dto.CartItemDTO;
import com.example.cdwebbackend.dto.ColorDTO;
import com.example.cdwebbackend.entity.CartEntity;
import com.example.cdwebbackend.entity.CartItemEntity;
import com.example.cdwebbackend.entity.ColorEntity;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.repository.CartItemRepository;
import com.example.cdwebbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class ColorConverter {
        public ColorEntity toEntity(ColorDTO colorDTO) {
            ColorEntity colorEntity = new ColorEntity();
            colorEntity.setId(colorDTO.getId());

            colorEntity.setName(colorDTO.getName());
            return colorEntity;

        }

    public ColorDTO toDTO(ColorEntity colorEntity) {
        ColorDTO colorDTO = new ColorDTO();
        colorDTO.setId(colorEntity.getId());

        colorDTO.setName(colorEntity.getName());
        return colorDTO;

    }



}

