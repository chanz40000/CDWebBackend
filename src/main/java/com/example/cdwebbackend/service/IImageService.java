package com.example.cdwebbackend.service;

import com.example.cdwebbackend.dto.ImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IImageService {
    // Phương thức upload hình ảnh
    ImageDTO uploadImage(MultipartFile file, Long entityId, String entityType) throws IOException;


}
