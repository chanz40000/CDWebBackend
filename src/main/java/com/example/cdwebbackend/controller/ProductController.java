package com.example.cdwebbackend.controller;

import com.example.cdwebbackend.converter.ProductConverter;
import com.example.cdwebbackend.dto.ProductDTO;
import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.example.cdwebbackend.repository.ProductRepository;
import com.example.cdwebbackend.service.impl.ImageUploadService;
import com.example.cdwebbackend.service.impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
//    @GetMapping("")
//    public ResponseEntity<String> getAllProduct{
//
//
//    }
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductConverter productConverter;

    @Autowired
    ImageUploadService imageUploadService;

    @Autowired
    ProductService productService;

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         @RequestParam("productId") long id) {

        try {
            //
            String url = imageUploadService.uploadFile(file);
            productService.uploadImage(id, url);

            Map<String, Object> response = Map.of("status", "success"
                    ,"message", "Image uploaded successfully",
                    "data", Map.of("url", url,
                            "productId", id));
            return ResponseEntity.ok(response);
        }catch (Exception e){
            Map<String, Object> error = Map.of(
              "status", "failed",
              "message", "Upload failed: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }
}
