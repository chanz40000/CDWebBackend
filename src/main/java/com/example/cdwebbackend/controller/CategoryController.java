package com.example.cdwebbackend.controller;

import com.example.cdwebbackend.dto.CategoryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/categories")
@Validated
public class CategoryController {
    //hien thi tat ca cac category
    @GetMapping("")
    public ResponseEntity<String> getAllCategories(@RequestParam("page") int page,
                                                   @RequestParam ("limit") int limit){

        return ResponseEntity.ok(String.format("getAllCategories, page = %d, limit = %d", page, limit));
    }

    //Neu tham so truyen vao la mot object thi sao? => Data Transfer Object = Request Object
    @PostMapping("")
    public ResponseEntity<?> insertCategory(@Validated @RequestBody CategoryDTO categoryDTO,
                                                 BindingResult result){
        if(result.hasErrors()){
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
             return ResponseEntity.badRequest().body(errorMessages);
        }
        return ResponseEntity.ok("this is insert category");
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id){
        return ResponseEntity.ok("this is insert category");
    }
    @DeleteMapping ("/{id}")
    public ResponseEntity<String> deleteCategory(){
        return ResponseEntity.ok("this is insert category");
    }
}
