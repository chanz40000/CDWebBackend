package com.example.cdwebbackend.controller;

import com.example.cdwebbackend.converter.UserConverter;
import com.example.cdwebbackend.dto.UserDTO;
import com.example.cdwebbackend.dto.UserLoginDTO;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.repository.UserRepository;
import com.example.cdwebbackend.responses.UserResponse;
import com.example.cdwebbackend.service.impl.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserConverter userConverter;
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Validated @RequestBody UserDTO userDTO ,BindingResult result){

        try {
             if(result.hasErrors()){
                 List<String> errorMessages = new ArrayList<>();
                 for (FieldError fieldError : result.getFieldErrors()) {
                     String defaultMessage = fieldError.getDefaultMessage();
                     errorMessages.add(defaultMessage);
                 }
                 return ResponseEntity.badRequest().body(errorMessages);
             }
             if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                 return ResponseEntity.badRequest().body("Password does not match");
             }
            UserEntity user = userService.createUser(userDTO);
             return ResponseEntity.ok("Register successfully");
         }catch (Exception e){
             return ResponseEntity.badRequest().body(e.getMessage());
         }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody UserLoginDTO userLoginDTO) {
        try {
            System.out.println("data -> " + userLoginDTO.getUsername());
            String token = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());

            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
           System.out.println("hellllo");
            return ResponseEntity.ok(token);  // Trả về token nếu đăng nhập thành công
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/details")
    public ResponseEntity<UserResponse> getUserDetails(@RequestHeader("Authorization") String token){
        System.out.println("qua toi day roi");
        try{
            String extractedToken = token.substring(7); //Loai bo "Bearer " tu chuoi token
            System.out.println("Loai bo bearer");
            UserEntity userEntity = userService.getUserDetailsFromToken(extractedToken);
            System.out.println("Return: "+ UserResponse.fromUser(userEntity));
            return ResponseEntity.ok(UserResponse.fromUser(userEntity));
        }catch (Exception e){
            System.out.println("Lỗi: " + e.getMessage()); // In lỗi ra console
            e.printStackTrace(); // In stacktrace đầy đủ
            return ResponseEntity.badRequest().build();
        }
    }

}
