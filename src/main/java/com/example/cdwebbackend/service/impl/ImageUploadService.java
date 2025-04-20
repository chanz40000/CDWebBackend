package com.example.cdwebbackend.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageUploadService {

    private final Cloudinary cloudinary;

    @Autowired
    public ImageUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file) throws IOException {

        byte[] bytes = file.getBytes();
        // Hash file bằng SHA1
        String sha1 = DigestUtils.sha1Hex(bytes);
        String publicId = "image_" + sha1; // public_id để so sánh môỗi tấm hình
        // Tránh lưu trùng ảnh

        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.asMap(
                    "public_id", publicId,
                    "overwrite", false,
                    "resource_type", "image"
            ));
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new IOException("Cloudinary upload failed: " + e.getMessage(), e);
        }
    }


}
