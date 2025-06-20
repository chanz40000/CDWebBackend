package com.example.cdwebbackend.controller;

import com.example.cdwebbackend.converter.ProductConverter;

import com.example.cdwebbackend.converter.ProductSizeColorConverter;
import com.example.cdwebbackend.dto.ColorDTO;
import com.example.cdwebbackend.dto.ProductDTO;
import com.example.cdwebbackend.dto.SizeDTO;
import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.dto.*;
import com.example.cdwebbackend.entity.*;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.example.cdwebbackend.repository.*;
import com.example.cdwebbackend.responses.ProductColorRespone;
import com.example.cdwebbackend.responses.ProductResponse;
import com.example.cdwebbackend.responses.ProductSizeColorRespone;
import com.example.cdwebbackend.service.impl.ImageUploadService;
import com.example.cdwebbackend.service.impl.ProductService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.*;
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

    @Autowired
    ProductColorRepository productColorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private ProductSizeColorRepository productSizeColorRepository;
    @Autowired
    ProductSizeColorConverter productSizeColorConverter;


    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         @RequestParam("productId") long productId,
                                         @RequestParam("colorId") long colorId){

        try {
            //
            String url = imageUploadService.uploadFile(file);
            productService.uploadImage(productId, colorId, url);

            Map<String, Object> response = Map.of("status", "success"
                    ,"message", "Image uploaded successfully",
                    "data", Map.of("url", url,
                            "productId", productId,
                            "colorId", colorId));
            return ResponseEntity.ok(response);
        }catch (Exception e){
            Map<String, Object> error = Map.of(
              "status", "failed",
              "message", "Upload failed: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProductWithImage(@RequestPart("product") String productJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDTO productDTO = objectMapper.readValue(productJson, ProductDTO.class); // Parse tay
            productDTO.setActive(false);
            ProductEntity saved = productService.createProduct(productDTO);
            ProductResponse response = ProductResponse.fromEntity(saved);
            return ResponseEntity.ok(Map.of(
                    "message", "Thêm sản phẩm thành công",
                    "productId", saved.getId(),
                    "data", response
            ));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Dữ liệu không hợp lệ",
                    "details", "Trường ID (categoryCode, brandCode, ...) phải là số"
            ));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Không tìm thấy dữ liệu",
                    "details", e.getMessage()
            ));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Lỗi parse JSON",
                    "details", e.getOriginalMessage()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Lỗi server",
                    "details", e.getMessage()
            ));
        }
    }

    @PutMapping(value = "/update/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProductWithImage(
            @PathVariable("productId") Long productId,
            @RequestPart("product") String productJson
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDTO productDTO = objectMapper.readValue(productJson, ProductDTO.class);

            // Gán productId cho DTO nếu cần
            productDTO.setId(productId);

            ProductEntity updated = productService.updateProduct(productDTO, productId);
            ProductResponse response = ProductResponse.fromEntity(updated);

            return ResponseEntity.ok(Map.of(
                    "message", "Cập nhật sản phẩm thành công",
                    "productId", updated.getId(),
                    "data", response
            ));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Dữ liệu không hợp lệ",
                    "details", "Trường ID (categoryCode, brandCode, ...) phải là số"
            ));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Không tìm thấy dữ liệu",
                    "details", e.getMessage()
            ));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Lỗi parse JSON",
                    "details", e.getOriginalMessage()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Lỗi server",
                    "details", e.getMessage()
            ));
        }
    }
//    @PutMapping( "/update-active-product/{productId}")
//    public ResponseEntity<?> updateActiveProduct(
//            @PathVariable("productId") Long productId,
//            @RequestParam("active") Boolean active
//    ) {
//        try {
//
//            ProductEntity entity = productRepository.findOneById(productId);
//            entity.setActive(active);
//
//            // Gán productId cho DTO nếu cần
//            productDTO.setId(productId);
//
//            ProductEntity updated = productService.updateProduct(productDTO, productId);
//            ProductResponse response = ProductResponse.fromEntity(updated);
//
//            return ResponseEntity.ok(Map.of(
//                    "message", "Cập nhật sản phẩm thành công",
//                    "productId", updated.getId(),
//                    "data", response
//            ));
//        } catch (NumberFormatException e) {
//            return ResponseEntity.badRequest().body(Map.of(
//                    "error", "Dữ liệu không hợp lệ",
//                    "details", "Trường ID (categoryCode, brandCode, ...) phải là số"
//            ));
//        } catch (DataNotFoundException e) {
//            return ResponseEntity.badRequest().body(Map.of(
//                    "error", "Không tìm thấy dữ liệu",
//                    "details", e.getMessage()
//            ));
//        } catch (JsonProcessingException e) {
//            return ResponseEntity.badRequest().body(Map.of(
//                    "error", "Lỗi parse JSON",
//                    "details", e.getOriginalMessage()
//            ));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
//                    "error", "Lỗi server",
//                    "details", e.getMessage()
//            ));
//        }
//    }
    @PostMapping("/update_information")
    public ResponseEntity<?> updateProductInformation(
            @RequestParam("productId") Long productId,
            @RequestParam("colorId") Long colorId,
            @RequestParam("sizeId") Long sizeId,
            @RequestParam("file") MultipartFile file, // Dùng MultipartFile để nhận file hình ảnh
            @RequestParam("stock") int stock
    ) {
        try {
            // Tải lên hình ảnh và nhận URL
            String imageUrl = imageUploadService.uploadFile(file);

            // Cập nhật thông tin sản phẩm với ảnh mới
            productService.chooseInfomation(productId, colorId, imageUrl, stock, sizeId);

            // Trả về phản hồi thành công
            return ResponseEntity.ok(Map.of(
                    "message", "Cập nhật thành công",
                    "productId", productId
            ));
        }  catch (Exception e) {
            // Trường hợp có lỗi trong quá trình xử lý
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Đã xảy ra lỗi khi cập nhật thông tin sản phẩm: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/change_active_product") public ResponseEntity<?> deleteProductById(
            @RequestParam("productId") Long productId,
            @RequestParam("active") Boolean active
    ){
        try {

            // Cập nhật thông tin sản phẩm với ảnh mới
            productService.changeActiveProduct(productId, active);
            // Trả về phản hồi thành công
            return ResponseEntity.ok(Map.of(
                    "message", "cập nhât thành công",
                    "productId", productId
            ));
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi chi tiết hơn
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Đã xảy ra lỗi khi cập nhật trạng thái sản phẩm: " + e.getMessage()
            ));
        }


    }

    @PutMapping("/change_active_product_size_color") public ResponseEntity<?> deleteProductSizeColorById(
            @RequestParam("productSizeColorId") Long productSizeColorId,
            @RequestParam("active") Boolean active
    ){
        try {



            // Cập nhật thông tin sản phẩm với ảnh mới
            productService.changeActiveProductSizeColor(productSizeColorId, active);


            // Trả về phản hồi thành công
            return ResponseEntity.ok(Map.of(
                    "message", "cập nhât thành công ",
                    "productId", productSizeColorId
            ));
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi chi tiết hơn
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Đã xảy ra lỗi khi cập nhật trạng thái sản phẩm: " + e.getMessage()
            ));
        }


    }

    @PostMapping("/add_size_by_color")
    public ResponseEntity<?> addSizeByColor(
            @RequestParam("productId") Long productId,
            @RequestParam("productColorId") Long productColorId,
            @RequestParam("sizeId") Long sizeId,
            @RequestParam("stock") int stock
    ){

        try {



            // Cập nhật thông tin sản phẩm với ảnh mới
            productService.addSizeByColor(productColorId, productId, sizeId, stock);
            ProductEntity product = productRepository.findOneById(productId);
            ProductResponse response = ProductResponse.fromEntity(product);

            // Trả về phản hồi thành công
            return ResponseEntity.ok(Map.of(
                    "message", "Thêm kích thước thành công",
                    "productId", response
            ));
        }  catch (Exception e) {
            // Trường hợp có lỗi trong quá trình xử lý
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Đã xảy ra lỗi khi cập nhật thông tin sản phẩm: " + e.getMessage()
            ));
        }

    }

    @PostMapping("/add_color_product")
    public ResponseEntity<?> addColorProduct(
            @RequestParam("productId") Long productId,
            @RequestParam("colorId") Long colorId,
            @RequestParam("file") MultipartFile file
    ){

        try {

// Tải lên hình ảnh và nhận URL
            String imageUrl = imageUploadService.uploadFile(file);

            // Cập nhật thông tin sản phẩm với ảnh mới
            productService.addColorProduct(productId, colorId, imageUrl);
            // Lấy danh sách màu sản phẩm sau khi cập nhật
            List<ProductColorEntity> productColorEntities = productColorRepository.findByProductId(productId);

            // Chuyển sang DTO
            List<ProductColorRespone> response = productColorEntities.stream()
                    .map(ProductColorRespone::fromEntity)
                    .toList();

            // Trả về phản hồi thành công
            return ResponseEntity.ok(Map.of(
                    "message", "Thêm màu sắc thành công",
                    "productId", response
            ));
        }  catch (Exception e) {
            // Trường hợp có lỗi trong quá trình xử lý
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Đã xảy ra lỗi khi cập nhật thông tin sản phẩm: " + e.getMessage()
            ));
        }

    }

    @GetMapping("/getCategory")
    public ResponseEntity<List<CategoryDTO>> getCategory() {
        try {
            List<CategoryEntity> categoryEntities = categoryRepository.findAll();
            System.out.println("Category count: " + categoryEntities.size());

            List<CategoryDTO> categoryDTOS = categoryEntities.stream()
                    .map(entity -> {
                        CategoryDTO dto = new CategoryDTO();
                        dto.setId(entity.getId());
                        dto.setName(entity.getName());
                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(categoryDTOS);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getBrand")
    public ResponseEntity<List<BrandDTO>> getBrand() {
        try {
            List<BrandEntity> brandEntities = brandRepository.findAll();

            List<BrandDTO> brandDTOS = brandEntities.stream()
                    .map(entity -> {
                        BrandDTO dto = new BrandDTO();
                        dto.setId(entity.getId());
                        dto.setName(entity.getName());
                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(brandDTOS);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getSize")
    public ResponseEntity<List<SizeDTO>> getSize() {
        try {
            List<SizeEntity> sizeEntities = sizeRepository.findAll();

            List<SizeDTO> sizeDTOS = sizeEntities.stream()
                    .map(entity -> {
                        SizeDTO dto = new SizeDTO();
                        dto.setId(entity.getId());
                        dto.setName(entity.getSize());
                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(sizeDTOS);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/getColor")
    public ResponseEntity<List<ColorDTO>> getColor() {
        try {
            List<ColorEntity> colorEntities = colorRepository.findAll();

            List<ColorDTO> colorDTOS = colorEntities.stream()
                    .map(entity -> {
                        ColorDTO dto = new ColorDTO();
                        dto.setId(entity.getId());
                        dto.setName(entity.getName());
                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(colorDTOS);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getColorProduct")
    public ResponseEntity<List<ProductColorDTO>> getColorProduct(
            @RequestParam("productId") Long productId
    ) {
        try {
            List<ProductColorEntity> productColorEntities = productColorRepository.findByProductId(productId);



            List<ProductColorDTO> productColorDTOS = productColorEntities.stream()
                    .map(entity -> {
                        ProductColorDTO dto = new ProductColorDTO();
                        ColorEntity color = colorRepository.findOneById(entity.getColor().getId());
                        dto.setId(entity.getId());
                        dto.setColorId(entity.getColor().getId());
                        dto.setProductId(productId);
                        dto.setImage(entity.getImage());
                        dto.setColor(color.getName());
                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(productColorDTOS);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getProductSizeColor")
    public ResponseEntity<Map<String, Object>> getProductSizeColor(
            @RequestParam("productId") Long productId
    ) {
        try {
            ProductEntity product = productRepository.findOneById(productId);
            List<ProductSizeColorEntity> productSizeColorEntities = productSizeColorRepository.findByProduct_Id(productId);

            // Nếu không tìm thấy dữ liệu, trả về phản hồi với thông báo không tìm thấy
            if (productSizeColorEntities.isEmpty()) {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "Không tìm thấy màu sắc và kích thước cho sản phẩm");
                responseBody.put("productId", productId);

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }

            // Chuyển sang DTO
            List<ProductSizeColorRespone> response = productSizeColorEntities.stream()
                    .map(ProductSizeColorRespone::fromEntity)
                    .toList();

            // Tạo một Map để trả về
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Lấy danh sách màu sắc thành công");
            responseBody.put("productId", productId);
            responseBody.put("nameProduct", product.getNameProduct());
            responseBody.put("price", product.getPrice());
            responseBody.put("data", response);

            // Trả về phản hồi với status 200 (OK)
            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



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
    @GetMapping("/list_page")
    public ResponseEntity<Map<String, Object>> getAllProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "3") int size,
            @RequestParam(value = "isActive", required = false) Boolean isActive
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductDTO> productPage;

            // ✅ Kiểm tra null (client không truyền isActive)
            if (isActive == null) {
                productPage = productService.getAllProductsPaginated(null, pageable); // lấy tất cả
            } else {
                productPage = productService.getAllProductsPaginated(isActive, pageable); // true hoặc false
            }

            List<ProductResponse> responseList = productPage.getContent().stream()
                    .map(dto -> ProductResponse.fromEntity(productConverter.toEntity(dto)))
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("products", responseList);
            response.put("currentPage", productPage.getNumber());
            response.put("totalItems", productPage.getTotalElements());
            response.put("totalPages", productPage.getTotalPages());
            response.put("pageSize", size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getProduct/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable("productId") Long productId,
                                            @RequestParam(value = "isActive", required = false) Boolean isActive) {
        try {
            ProductDTO productDTO;

            if (isActive != null && isActive) {
                productDTO = productService.getProductByIdTrue(productId);
            } else {
                productDTO = productService.getProductById(productId); // lấy không quan tâm isActive
            }
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
    @GetMapping("/getListColor/{productId}")
    public ResponseEntity<?> getListColorByProductId(@PathVariable("productId") Long productId) {
        try {
            List<ColorDTO>response = productService.getListColorByIdProduct(productId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving the product.");
        }
    }
    @GetMapping("/getListSize/{productId}")
    public ResponseEntity<?> getListSizeByProductId(@PathVariable("productId") Long productId) {
        try {
            List<SizeDTO>response = productService.getListSizeByIdProduct(productId);
            System.out.println(response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving the product.");
        }
    }

    @GetMapping("/getProductName/{productName}")
    public ResponseEntity<?> getProductByName(@PathVariable("productName") String productName) {
        try {
            List<ProductDTO> productDTOs = productService.getProductByName(productName);
            if (productDTOs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No product found with id = " + productName);
            }
            List<ProductResponse> response =new ArrayList<>();
            for (ProductDTO p: productDTOs){
                response.add(ProductResponse.fromEntity(productConverter.toEntity(p)));
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving the product.");
        }
    }
    //lay id cua product_color_size
    @GetMapping("/getIdProductSizeColor")
    public ResponseEntity<?> getProductSizeColorId(
            @RequestParam("productId") Long productId,
            @RequestParam("colorId") Long colorId,
            @RequestParam("sizeId") Long sizeId
    ) {
        try {
            Long productSizeColorId = productService.getProductSizeColorId(productId, colorId, sizeId);
            if (productSizeColorId != null) {
                return ResponseEntity.ok(productSizeColorId);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy tổ hợp sản phẩm với màu và kích thước này.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy ID tổ hợp sản phẩm: " + e.getMessage());
        }
    }

    @GetMapping("/getProductSizeColor/{id}")
    public ResponseEntity<?> getProductSizeColorById(@PathVariable("id") Long id) {
        try {
            ProductSizeColorEntity productSizeColor = productSizeColorRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy ProductSizeColor với ID: " + id));

            // Tạo DTO để trả về
            ProductSizeColorDTO dto = new ProductSizeColorDTO();
            dto.setId(productSizeColor.getId());
            dto.setProductId(productSizeColor.getProduct().getId());
            dto.setColorCode(productSizeColor.getSize().getId());
            dto.setSizeCode(productSizeColor.getSize().getId());

            return ResponseEntity.ok(dto);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi khi lấy chi tiết ProductSizeColor: " + e.getMessage()));
        }
    }

    @GetMapping("/getNameSizeColor/{id-product-size-color}")
    public ResponseEntity<?>getNameSizeColor(@PathVariable("id-product-size-color") Long idProductSizeColor){
        try {
            Map<String, String>size_color = productSizeColorRepository.findSizeAndColorNamesById(idProductSizeColor).get();
            System.out.println("colorName"+size_color.get("colorName"));
            System.out.println("sizeName"+size_color.get("sizeName"));
            return ResponseEntity.ok(size_color);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }

    }
    @GetMapping("/getProductsWithZeroStock")
    public ResponseEntity<?>getProductsWithZeroStock(){
        try{
            List<ProductSizeColorDTO>result = new ArrayList<>();
            List<ProductSizeColorEntity> productEntityList = productService.getProductsWithZeroStockPurchasedInLastThreeMonths();
            for (ProductSizeColorEntity p: productEntityList){
                System.out.println(productSizeColorConverter.toDTO(p));
                result.add(productSizeColorConverter.toDTO(p));
            }

            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/getImageByProductSizeColorId/{id}")
    public ResponseEntity<?>getImageByProductSizeColorId(@PathVariable("id") Long idProductSizeColor){
        try{
            String image = productService.getImageByProductSizeColorId(idProductSizeColor);
            return ResponseEntity.ok(image);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }


}
