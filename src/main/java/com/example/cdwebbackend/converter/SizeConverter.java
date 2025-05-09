package com.example.cdwebbackend.converter;

import com.example.cdwebbackend.dto.ColorDTO;
import com.example.cdwebbackend.dto.SizeDTO;
import com.example.cdwebbackend.entity.ColorEntity;
import com.example.cdwebbackend.entity.SizeEntity;
import org.springframework.stereotype.Component;

@Component
public class SizeConverter {
        public SizeEntity toEntity(SizeDTO sizeDTO) {
            SizeEntity sizeEntity = new SizeEntity();
            sizeEntity.setId(sizeDTO.getId());

            sizeEntity.setSize(sizeDTO.getName());
            return sizeEntity;

        }

    public SizeDTO toDTO(SizeEntity sizeEntity) {
        SizeDTO sizeDTO = new SizeDTO();
        sizeDTO.setId(sizeEntity.getId());

        sizeDTO.setName(sizeEntity.getSize());
        return sizeDTO;

    }



}

