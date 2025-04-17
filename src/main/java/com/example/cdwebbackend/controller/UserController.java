package com.example.cdwebbackend.controller;

import com.example.cdwebbackend.components.JwtTokenUtil;
import com.example.cdwebbackend.converter.UserConverter;
import com.example.cdwebbackend.dto.UserDTO;
import com.example.cdwebbackend.dto.UserLoginDTO;
import com.example.cdwebbackend.entity.RoleEntity;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.example.cdwebbackend.payload.ResponseObject;
import com.example.cdwebbackend.repository.RoleRepository;
import com.example.cdwebbackend.repository.UserRepository;
import com.example.cdwebbackend.responses.UserResponse;
import com.example.cdwebbackend.service.AuthService;
import com.example.cdwebbackend.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.*;

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

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthService authService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;


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
            String token = userService.login(userLoginDTO);

            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
            Map<String, String> response = new HashMap<>();
            response.put("accessToken", token);
            return ResponseEntity.ok(response);  // Trả về token nếu đăng nhập thành công
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
//@PostMapping("/login")
//public String login(@Validated @RequestBody UserLoginDTO userLoginDTO) throws Exception{
//    Optional<UserEntity> optionalUser = Optional.empty();
//    String subject = null;
//    RoleEntity roleUser = roleRepository.findOneById(1)
//            .orElseThrow(()->new DataNotFoundException(localizatonUtils.getLocalizedMessage(MessageKeys.ROLE_DOES_NOT_EXISTS)));
//    if(userLoginDTO.getGoogleAccountId()!=null &&userLoginDTO.isGoogleAccountIdValid()){
//        subject = "Google:"+userLoginDTO.getGoogleAccountId();
//        if(optionalUser.isEmpty()){
//            UserEntity userEntity = userConverter.toEntity(userLoginDTO);
//            userEntity = userRepository.save(userEntity);
//            optionalUser=Optional.of(userEntity);
//        }
//        Map<String, Object>attributes = new HashMap<>();
//        attributes.put("email", userLoginDTO.getEmail());
//        return j
//    }
//}

    @PostMapping("/details")
    public ResponseEntity<UserResponse> getUserDetails(@RequestHeader("Authorization") String authorizationHeader){

        try{
            String extractedToken = authorizationHeader.substring(7); //Loai bo "Bearer " tu chuoi token
            UserEntity userEntity = userService.getUserDetailsFromToken(extractedToken);
            return ResponseEntity.ok(UserResponse.fromUser(userEntity));
        }catch (Exception e){
            System.out.println("Lỗi: " + e.getMessage()); // In lỗi ra console
            e.printStackTrace(); // In stacktrace đầy đủ
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/details/{userId}")
    public ResponseEntity<UserResponse> updateUserDetails
            (@PathVariable("userId")  int userId,
             @RequestBody UserDTO updateUserDTO,
             @RequestHeader("Authorization") String authorizationHeader){
        try{
            String extractedToken = authorizationHeader.substring(7); //Loai bo "Bearer " tu chuoi token
            UserEntity userEntity = userService.getUserDetailsFromToken(extractedToken);

            if(userEntity.getId()!=userId){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            UserEntity user = userService.updateUser(updateUserDTO, userId);
            return ResponseEntity.ok(UserResponse.fromUser(userEntity));
        }catch (Exception e){
            System.out.println("Lỗi: " + e.getMessage()); // In lỗi ra console
            e.printStackTrace(); // In stacktrace đầy đủ
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("/changePassword/{userId}")
    public ResponseEntity<UserResponse> updatePassword(
            @PathVariable("userId") int userId,
            @RequestBody Map<String, String> passwords,
            @RequestHeader("Authorization") String authorizationHeader) {
        System.out.println("sua mat khau");

        try {
            String extractedToken = authorizationHeader.substring(7); // "Bearer " bỏ đi
            UserEntity userEntity = userService.getUserDetailsFromToken(extractedToken);

            if (userEntity.getId() != userId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            String oldPassword = passwords.get("oldPassword");
            String newPassword = passwords.get("newPassword");


            if (!userService.checkPassword(oldPassword, userEntity.getPassword())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            UserEntity user = userService.updatePassword(newPassword, userId);
            userRepository.save(user);
            return ResponseEntity.ok(UserResponse.fromUser(user));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    //bam dang nhap google, redirect den trang dang nhap google, dang nhap xong co code
    //tu code => google token =>lay ra cac thong tin khac
    @GetMapping("/auth/social-login")
    public ResponseEntity<String> socialAuth(@RequestParam("login_type")String loginType,
                                             HttpServletRequest request){
        System.out.println("login_type: "+ loginType + "-----request: "+ request);
        loginType =loginType.trim().toLowerCase();
        String url = authService.generateAuthUrl(loginType, request);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/auth/social/callback")
    public ResponseEntity<ResponseObject> callback(@RequestParam("code") String code,
                                                   @RequestParam("login_type") String loginType,
                                                   HttpServletRequest request) throws Exception {
        System.out.println("da qua toi google callback");
        Map<String, Object> userInfo = authService.authenticateAndFetchProfile(code, loginType);

        if (userInfo == null) {
            return ResponseEntity.badRequest().body(new ResponseObject(null, "Failed to authenticate", HttpStatus.BAD_REQUEST));
        }

        // 👉 Ở đây cần generate JWT và trả về
        String email = (String) userInfo.get("email");
        UserEntity userOpt = userRepository.findOneByEmail(email);
        if (userOpt==null) {
            return ResponseEntity.badRequest().body(new ResponseObject(null, "User not found", HttpStatus.BAD_REQUEST));
        }

        String token = jwtTokenUtil.generateToken(userOpt);
        System.out.println("token tra ve khi dang nhap email: "+ token);
        return ResponseEntity.ok(
                new ResponseObject("success", "Authenticated successfully", Map.of(
                        "token", token,
                        "user", userOpt
                ))
        );
    }


}
