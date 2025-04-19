package com.example.cdwebbackend.config;

import com.cloudinary.Cloudinary;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);

        try {
            return new Cloudinary(config);  // Kiểm tra khởi tạo đúng
        } catch (Exception e) {
            e.printStackTrace();  // In lỗi để biết chi tiết
            throw new RuntimeException("Error initializing Cloudinary", e);
        }
    }
}
