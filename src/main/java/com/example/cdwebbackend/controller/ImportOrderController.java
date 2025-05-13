package com.example.cdwebbackend.controller;

import com.example.cdwebbackend.dto.ImportOrderDTO;
import com.example.cdwebbackend.dto.ImportOrderProductDTO;
import com.example.cdwebbackend.dto.ProductDTO;
import com.example.cdwebbackend.dto.UserDTO;
import com.example.cdwebbackend.entity.*;
import com.example.cdwebbackend.repository.ImportOrderProductRepository;
import com.example.cdwebbackend.repository.ImportOrderRepository;
import com.example.cdwebbackend.repository.ProductRepository;
import com.example.cdwebbackend.repository.ProductSizeColorRepository;
import com.example.cdwebbackend.responses.ProductResponse;
import com.example.cdwebbackend.service.impl.ImportOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping("${api.prefix}/importOrder")
public class ImportOrderController {
    @Autowired
    ImportOrderService importOrderService;


    @PostMapping("/insert")
    public ResponseEntity<?> createImportOrder(@Validated @RequestBody ImportOrderDTO importOrderDTO , BindingResult result){

        System.out.println("vao chuc nang nhap hang");
        try {
            if(result.hasErrors()){
                List<String> errorMessages = new ArrayList<>();
                for (FieldError fieldError : result.getFieldErrors()) {
                    String defaultMessage = fieldError.getDefaultMessage();
                    errorMessages.add(defaultMessage);
                }
                return ResponseEntity.badRequest().body(errorMessages);
            }
            importOrderService.insert(importOrderDTO);
            return ResponseEntity.ok("Them don hang thành công");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteImportOrder(@RequestBody Map<String, Long> requestBody) {
        try {
            // Lấy ID từ JSON request
            Long id = requestBody.get("id");

            // Kiểm tra xem ID có hợp lệ không
            if (id == null) {
                return ResponseEntity.badRequest().body("ID không hợp lệ");
            }
            //ImportOrderEntity importOrderEntity = importOrderRepository.findOneById(id).get();
            // Xóa đơn nhập hàng theo ID
            importOrderService.deleteById(id);

            return ResponseEntity.ok("Xoá đơn nhập hàng thành công");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


@PostMapping("/update")
public ResponseEntity<?> updateImportOrder(@Validated @RequestBody ImportOrderDTO importOrderIdDTO, BindingResult result) {
    System.out.println("Vào chức năng sửa đơn hàng");
    try {
        if (result.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (FieldError fieldError : result.getFieldErrors()) {
                String defaultMessage = fieldError.getDefaultMessage();
                errorMessages.add(defaultMessage);
            }
            return ResponseEntity.badRequest().body(errorMessages);
        }

        // Kiểm tra nếu không có id trong request body
        if (importOrderIdDTO.getId() == null) {
            return ResponseEntity.badRequest().body("ID đơn hàng không hợp lệ");
        }

        importOrderService.update(importOrderIdDTO);
        return ResponseEntity.ok("Sửa đơn hàng thành công");
    } catch (Exception e) {
        System.out.println(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
   @GetMapping("/list")
    public ResponseEntity<?>getAllImportOrder(){
       try {
           // Giả sử productService.getAllProducts() trả về List<ProductDTO>
           List<ImportOrderDTO> responseList = importOrderService.selectAll();
           System.out.println("Danh sách đơn nhập: " + responseList.toString());
           return ResponseEntity.ok(responseList);
       } catch (Exception e) {
           System.out.println("Lỗi: " + e.getMessage());
           e.printStackTrace();
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }
   }


}
