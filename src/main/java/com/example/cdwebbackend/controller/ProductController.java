package com.example.cdwebbackend.controller;

import com.example.cdwebbackend.converter.ProductConverter;
import com.example.cdwebbackend.dto.ProductDTO;
import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.example.cdwebbackend.repository.ProductRepository;
import com.example.cdwebbackend.responses.ProductResponse;
import com.example.cdwebbackend.service.impl.ImageUploadService;
import com.example.cdwebbackend.service.impl.ProductService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
//     * @param productDTO
//     * @param result
//     * @return
     */

//    @PostMapping("/add")
//    public ResponseEntity<?> addProduct(@Validated @RequestBody ProductDTO productDTO, BindingResult result) {
//        try {
//
//            if (result.hasErrors()) {
//                List<String> errors = new ArrayList<>();
//                for (FieldError error : result.getFieldErrors()) {
//                    errors.add(error.getDefaultMessage());
//                }
//                return ResponseEntity.badRequest().body(errors);
//            }
//
//            ProductEntity product = productService.createProduct(productDTO);
//            return ResponseEntity.ok(Map.of(
//                    "message", "Product created successfully",
//                    "productId", product.getId()
//            ));
//
//
//        } catch (DataNotFoundException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
//        }
//    }
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProductWithImage(
            @RequestPart("product") String productJson, // <-- nhận dưới dạng String
            @RequestPart(value = "file", required = false) MultipartFile file,
            Authentication authentication
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDTO productDTO = objectMapper.readValue(productJson, ProductDTO.class); // Parse tay

            if (file != null && !file.isEmpty()) {
                String imageUrl = imageUploadService.uploadFile(file);
                productDTO.setImageUrl(imageUrl);
            }

            ProductEntity saved = productService.createProduct(productDTO);
            return ResponseEntity.ok(Map.of(
                    "message", "Thêm sản phẩm thành công",
                    "productId", saved.getId()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi khi parse hoặc xử lý sản phẩm");
        }
    }


//    @GetMapping("/list")
//    public ResponseEntity<?> getAllProducts() {
//        try {
//            List<ProductDTO> products = productService.getAllProducts();
//            return ResponseEntity.ok(products);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch products");
//        }
//    }
@GetMapping("/list")
public ResponseEntity<List<ProductResponse>> getAllProducts() {
    try {
        // Giả sử productService.getAllProducts() trả về List<ProductDTO>
        List<ProductDTO> products = productService.getAllProducts();

        // Chuyển List<ProductDTO> → List<ProductResponse>
        List<ProductResponse> responseList = products.stream()
                .map(productDTO -> ProductResponse.fromEntity(productConverter.toEntity(productDTO))) // convert DTO -> Entity -> Response
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    } catch (Exception e) {
        System.out.println("Lỗi: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

//    @PostMapping("/getProduct")
//    public ResponseEntity<?> getProductById(@RequestBody Map<String, Object> requestBody) {
//        try {
//            // Lấy id từ requestBody
//            long longId = Long.parseLong(requestBody.get("productId").toString());
//
//            ProductDTO productDTO = productService.getProductById(longId);
//            if (productDTO == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No product found with id = " + longId);
//            }
//            ProductResponse response = ProductResponse.fromEntity(productConverter.toEntity(productDTO));
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving the product.");
//        }
//    }

    @GetMapping("/getProduct/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable("productId") Long productId) {
        try {
            ProductDTO productDTO = productService.getProductById(productId);
            if (productDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No product found with id = " + productId);
            }
            ProductResponse response = ProductResponse.fromEntity(productConverter.toEntity(productDTO));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving the product.");
        }
    }


}
