package com.example.cdwebbackend.controller;

import com.example.cdwebbackend.components.JwtTokenUtil;
import com.example.cdwebbackend.converter.UserConverter;
import com.example.cdwebbackend.dto.*;
import com.example.cdwebbackend.entity.RoleEntity;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.example.cdwebbackend.payload.ResponseObject;
import com.example.cdwebbackend.repository.RoleRepository;
import com.example.cdwebbackend.repository.UserRepository;
import com.example.cdwebbackend.responses.UserResponse;
import com.example.cdwebbackend.service.AuthService;
import com.example.cdwebbackend.service.impl.EmailSenderService;
import com.example.cdwebbackend.service.impl.ImageUploadService;
import com.example.cdwebbackend.service.impl.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    private ImageUploadService imageUploadService;
    @Autowired
    EmailSenderService senderService;

//    @PostMapping("/register")
//    public ResponseEntity<?> createUser(@Valid @Validated @RequestBody UserDTO userDTO , BindingResult result){
//
//        try {
//             if(result.hasErrors()){
//                 List<String> errorMessages = new ArrayList<>();
//                 for (FieldError fieldError : result.getFieldErrors()) {
//                     String defaultMessage = fieldError.getDefaultMessage();
//                     errorMessages.add(defaultMessage);
//                 }
//                 return ResponseEntity.badRequest().body(errorMessages);
//             }
//             if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
//                 return ResponseEntity.badRequest().body("Password không khớp");
//             }
//            UserEntity user = userService.createUser(userDTO);
//             return ResponseEntity.ok("Đăng ký thành công");
//         }catch (Exception e){
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//    }
@PostMapping("/register")
public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO) {
    try {
        // Kiểm tra trùng lặp username và email
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("username", "Tên đăng nhập đã tồn tại"));
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("email", "Email đã tồn tại"));
        }

        // Lưu người dùng
        UserEntity user = userService.createUser(userDTO);
        return new ResponseEntity<>(userConverter.toDTO(user), HttpStatus.CREATED);
    } catch (Exception e) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
    }
}

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String token = userService.login(userLoginDTO);
             System.out.println("vao login");
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
    @PostMapping("/forgotPass")
    public ResponseEntity<?>forgot(@Validated @RequestBody ForgotPasswordDTO forgotPasswordDTO, HttpServletResponse response){
        System.out.println("vao forgot password");
        try {
            System.out.println(forgotPasswordDTO.getEmail());
            String email = forgotPasswordDTO.getEmail();
            //kiem tra xem email da duoc dang ky chua
            UserEntity userEntity = userRepository.findOneByEmail(email);
            if(userEntity==null){
                System.out.println("Email chưa được đăng ký");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email chưa được đăng ký");
            }
            //tao otp

            Random rand = new Random();
            int otpvalue = rand.nextInt(1255650);
            System.out.println(otpvalue);
            // sending otp
            senderService.sendEmail(email,
                "This is your otp: ",
                otpvalue+"");

            // Lưu OTP vào cookie
            Cookie otpCookie = new Cookie("otp", otpvalue+"");
            //otpCookie.setHttpOnly(true);
            otpCookie.setSecure(true); // Chỉ hoạt động với HTTPS
            otpCookie.setPath("/");
            otpCookie.setMaxAge(15 * 60); // OTP hết hạn sau 15 phút
            response.addCookie(otpCookie);

            // Lưu email vào cookie
            Cookie emailCookie = new Cookie("email", email);
            //emailCookie.setHttpOnly(true);
            emailCookie.setSecure(true);
            emailCookie.setPath("/");
            emailCookie.setMaxAge(15 * 60);
            response.addCookie(emailCookie);

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "OTP đã được gửi thành công");
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }

    }

    @PostMapping("/validateOtp")
    public ResponseEntity<?> validateOtp(@RequestBody Map<String, String> request,
                                         @CookieValue(value = "otp", required = false) String otpFromCookie,
                                         @CookieValue(value = "email", required = false) String emailFromCookie) {
        try {
            String inputOtp = request.get("otp");

            // Kiểm tra xem OTP từ cookie có tồn tại không
            if (otpFromCookie == null || emailFromCookie == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("OTP hoặc email không tồn tại hoặc đã hết hạn");
            }

            // So sánh OTP từ người dùng với OTP trong cookie
            if (inputOtp != null && inputOtp.equals(otpFromCookie)) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Xác thực OTP thành công");
                response.put("email", emailFromCookie);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("OTP không đúng");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi: " + e.getMessage());
        }
    }
    //response
//    @PutMapping("/newPassword")
//    public ResponseEntity<?> newPassword(@Validated @RequestBody Map<String, String> request,
//                                         @CookieValue(value = "email", required = false) String emailFromCookie,
//                                         HttpServletResponse response) {
//        System.out.println("Cập nhật mật khẩu mới");
//
//        try {
//            String email = request.get("email");
//            String newPassword = request.get("newPassword");
//
//            // Kiểm tra email từ cookie và body
//            if (emailFromCookie == null || !emailFromCookie.equals(email)) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body("Phiên xác thực không hợp lệ hoặc đã hết hạn.");
//            }
//
//            // Kiểm tra dữ liệu đầu vào
//            if (newPassword == null || newPassword.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body("Mật khẩu mới không được để trống.");
//            }
//
//            // Cập nhật mật khẩu
//            UserEntity user = userService.updatePassword(newPassword, email);
//
//            // Xóa cookie sau khi cập nhật thành công
//            Cookie emailCookie = new Cookie("email", null);
//            emailCookie.setHttpOnly(true);
//            emailCookie.setSecure(true);
//            emailCookie.setPath("/");
//            emailCookie.setMaxAge(0); // Xóa cookie
//            response.addCookie(emailCookie);
//
//            Cookie otpCookie = new Cookie("otp", null);
//            otpCookie.setHttpOnly(true);
//            otpCookie.setSecure(true);
//            otpCookie.setPath("/");
//            otpCookie.setMaxAge(0); // Xóa cookie
//            response.addCookie(otpCookie);
//
//            return ResponseEntity.ok(userConverter.toDTO(user));
//
//        } catch (DataNotFoundException e) {
//            System.out.println("Lỗi: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Người dùng không tồn tại: " + e.getMessage());
//        } catch (Exception e) {
//            System.out.println("Lỗi: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Lỗi khi cập nhật mật khẩu: " + e.getMessage());
//        }
//    }
    @PutMapping("/newPassword")
    public ResponseEntity<?> newPassword(
            @Valid @RequestBody NewPasswordDTO newPasswordDTO,
            @CookieValue(value = "email", required = false) String emailFromCookie,
            HttpServletResponse response) {
        System.out.println("Cập nhật mật khẩu mới");

        try {
            // Kiểm tra email từ cookie và body
            if (emailFromCookie == null || !emailFromCookie.equals(newPasswordDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Phiên xác thực không hợp lệ hoặc đã hết hạn.");
            }

            // Cập nhật mật khẩu
            UserEntity user = userService.updatePassword(newPasswordDTO.getNewPassword(), newPasswordDTO.getEmail());

            // Xóa cookie sau khi cập nhật thành công
            Cookie emailCookie = new Cookie("email", null);
            emailCookie.setHttpOnly(true);
            emailCookie.setSecure(true);
            emailCookie.setPath("/");
            emailCookie.setMaxAge(0); // Xóa cookie
            response.addCookie(emailCookie);

            Cookie otpCookie = new Cookie("otp", null);
            otpCookie.setHttpOnly(true);
            otpCookie.setSecure(true);
            otpCookie.setPath("/");
            otpCookie.setMaxAge(0); // Xóa cookie
            response.addCookie(otpCookie);

            return ResponseEntity.ok(userConverter.toDTO(user));

        } catch (DataNotFoundException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Người dùng không tồn tại: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật mật khẩu: " + e.getMessage());
        }
    }
    //response user
    @PostMapping("/details")
    public ResponseEntity<UserDTO> getUserDetails(@RequestHeader("Authorization") String authorizationHeader){
        System.out.println("thong tin user: ");
        try{
            String extractedToken = authorizationHeader.substring(7); //Loai bo "Bearer " tu chuoi token
            UserEntity userEntity = userService.getUserDetailsFromToken(extractedToken);
            System.out.println("thong tin user: "+ userEntity.toString());
            return ResponseEntity.ok(userConverter.toDTO(userEntity));
        }catch (Exception e){
            System.out.println("Lỗi: " + e.getMessage()); // In lỗi ra console
            e.printStackTrace(); // In stacktrace đầy đủ
            return ResponseEntity.badRequest().build();
        }
    }

    //response user
//@PutMapping("/details/{userId}")
//public ResponseEntity<?> updateUserDetails(
//        @PathVariable("userId") int userId,
//        @Valid @RequestBody UserDTO updateUserDTO,
//        @RequestHeader("Authorization") String authorizationHeader) {
//    try {
//        String extractedToken = authorizationHeader.substring(7);
//        UserEntity userEntity = userService.getUserDetailsFromToken(extractedToken);
//
//        if (userEntity.getId() != userId) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body("Bạn không có quyền cập nhật thông tin người dùng này.");
//        }
//
//        UserEntity updatedUser = userService.updateUser(updateUserDTO, userId);
//        return ResponseEntity.ok(userConverter.toDTO(updatedUser));
//
//    } catch (DataNotFoundException | DataIntegrityViolationException e) {
//        return ResponseEntity.badRequest().body(e.getMessage());
//    } catch (Exception e) {
//        e.printStackTrace();
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("Đã xảy ra lỗi hệ thống: " + e.getMessage());
//    }
//}
    @PutMapping("/details/{userId}")
    public ResponseEntity<?> updateUserDetails(
            @PathVariable("userId") int userId,
            @Valid @RequestBody UserUpdateDTO updateUserDTO,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            System.out.println("date: "+ updateUserDTO.getBirthday().toString());
            String extractedToken = authorizationHeader.substring(7);
            UserEntity userEntity = userService.getUserDetailsFromToken(extractedToken);

            if (userEntity.getId() != userId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Bạn không có quyền cập nhật thông tin người dùng này.");
            }

            UserEntity updatedUser = userService.updateUser(updateUserDTO, userId);
            return ResponseEntity.ok(userConverter.toDTO(updatedUser));

        } catch (DataNotFoundException | DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi hệ thống: " + e.getMessage());
        }
    }


//doi userresponse
//    @PutMapping("/changePassword/{userId}")
//    public ResponseEntity<UserDTO> updatePassword(
//            @PathVariable("userId") int userId,
//            @RequestBody Map<String, String> passwords,
//            @RequestHeader("Authorization") String authorizationHeader) {
//        System.out.println("sua mat khau");
//
//        try {
//            String extractedToken = authorizationHeader.substring(7); // "Bearer " bỏ đi
//            UserEntity userEntity = userService.getUserDetailsFromToken(extractedToken);
//
//            if (userEntity.getId() != userId) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//            }
//
//            String oldPassword = passwords.get("password");
//            String newPassword = passwords.get("newPassword");
//
//
//            if (!userService.checkPassword(oldPassword, userEntity.getPassword())) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//            }
//
//            UserEntity user = userService.updatePassword(newPassword, userId);
//            userRepository.save(user);
//            return ResponseEntity.ok(userConverter.toDTO(user));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().build();
//        }
//    }
@PutMapping("/changePassword/{userId}")
public ResponseEntity<?> updatePassword(
        @PathVariable("userId") int userId,
        @Valid @RequestBody UserUpdatePassDTO userDTO,
        @RequestHeader("Authorization") String authorizationHeader) {
    System.out.println("sua mat khau");

    try {
        String extractedToken = authorizationHeader.substring(7);
        UserEntity userEntity = userService.getUserDetailsFromToken(extractedToken);

        if (userEntity.getId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Bạn không có quyền cập nhật mật khẩu cho ID này.");
        }

        String oldPassword = userDTO.getPassword();
        String newPassword = userDTO.getNewPassword();

        if (!userService.checkPassword(oldPassword, userEntity.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Mật khẩu cũ không đúng.");
        }

        UserEntity user = userService.updatePassword(newPassword, userId);
        userRepository.save(user);
        return ResponseEntity.ok(userConverter.toDTO(user));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest()
                .body("Đã xảy ra lỗi khi cập nhật mật khẩu: " + e.getMessage());
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

    @PostMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {

            // Lấy thông tin người dùng từ SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Tìm user theo username
            UserEntity user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));


            String url = imageUploadService.uploadFile(file);
            userService.updateAvatar(user.getId(), url);

            Map<String, Object> response = Map.of(
                    "status", "success",
                    "message", "Image uploaded successfully",
                    "data", Map.of(
                            "url", url,
                            "userId", user.getId()
                    )
            );

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            Map<String, Object> error = Map.of(
                    "status", "fail",
                    "message", "Upload failed: " + e.getMessage(),
                    "data", null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (DataNotFoundException e) {
            Map<String, Object> error = Map.of(
                    "status", "fail",
                    "message", "User not found",
                    "data", null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }


}
