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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    /**
     * cách test api
     * {
     *   "name_product": "Áo hoodie",
     *   "description": "Mẫu áo hoodie mùa đông",
     *   "stock": 100,
     *   "price": 300000,
     *   "image": "link.jpg",
     *   "category_id": "1",
     *   "brand_id": "1",
     *   "productSizeColorDTOS": [
     *     {
     *       "sizeCode": 1,
     *       "colorCode": 1,
     *       "stock": 100
     *     }
     *
     *   ]
     * }
     * @param productDTO
     * @param result
     * @return
     */

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@Validated @RequestBody ProductDTO productDTO, BindingResult result) {
        try {

            if (result.hasErrors()) {
                List<String> errors = new ArrayList<>();
                for (FieldError error : result.getFieldErrors()) {
                    errors.add(error.getDefaultMessage());
                }
                return ResponseEntity.badRequest().body(errors);
            }

            ProductEntity product = productService.createProduct(productDTO);
            return ResponseEntity.ok(Map.of(
                    "message", "Product created successfully",
                    "productId", product.getId()
            ));


        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllProducts() {
        try {
            List<ProductDTO> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch products");
        }
    }

}
