package com.example.cdwebbackend.security;

import com.example.cdwebbackend.components.JwtTokenUtil;
import com.example.cdwebbackend.controller.UserController;
import com.example.cdwebbackend.dto.UserLoginDTO;
import com.example.cdwebbackend.entity.RoleEntity;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.repository.RoleRepository;
import com.example.cdwebbackend.repository.UserRepository;
import com.example.cdwebbackend.service.IUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

//    private final JwtTokenUtil jwtTokenUtil;
//    private final UserController userController;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        Authentication authentication) throws IOException {
//        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
//        String email = oauth2User.getAttribute("email");
//        String googleId = oauth2User.getAttribute("sub");
//
//        try {
//            UserEntity userEntity = new UserEntity();
//            userEntity.setEmail(email);
//            userEntity.setGoogleAccountId(googleId);
//            userEntity.setUsername(email);
//            String token = jwtTokenUtil.generateToken(userEntity);
//
//            if (token == null || token.isEmpty()) {
//                response.sendRedirect("http://localhost:3000/login?error");
//                return;
//            }
//
//            Cookie cookie = new Cookie("token", token);
//            cookie.setPath("/");
//            cookie.setHttpOnly(true);
//            cookie.setMaxAge(60 * 60 * 24); // Token hết hạn sau 1 ngày
//            response.addCookie(cookie);
//
//            // Log xem token có hợp lệ không
//            System.out.println("OAuth2 login for email: " + email);
//
//            System.out.println("Generated Token: " + token);
////            response.sendRedirect("http://localhost:3000/home");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.sendRedirect("http://localhost:3000/login?error");
//        }
//    }
private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String googleId = oauth2User.getAttribute("sub");

        try {
            // 1. Kiểm tra xem user đã tồn tại chưa
            UserEntity userEntity = userRepository.findOneByEmail(email);

            if (userEntity == null) {
                // 2. Nếu chưa có, tạo mới user
                userEntity = new UserEntity();
                userEntity.setEmail(email);

                String username = email.split("@")[0];
                userEntity.setUsername(username);
                userEntity.setGoogleAccountId(googleId);

                RoleEntity roleUser = roleRepository.findOneByName("USER")
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy role ROLE_USER"));

                userEntity.setRoles(List.of(roleUser));

                userRepository.save(userEntity);
                System.out.println("✅ Đã tạo user mới: " + email);
            } else {
                System.out.println("🔁 User đã tồn tại: " + email);
            }

            // 3. Tạo token cho user (đã có trong DB)
            String token = jwtTokenUtil.generateToken(userEntity);

            if (token == null || token.isEmpty()) {
                response.sendRedirect("http://localhost:3000/login?error");
                return;
            }

            // 4. Lưu token vào cookie
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 60 * 24);
            response.addCookie(cookie);

            System.out.println("OAuth2 login for email: " + email);
            System.out.println("Generated Token: " + token);

            // 5. Chuyển hướng về trang chủ
            response.sendRedirect("http://localhost:3000/oauth2/success?token=" + token);


        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("http://localhost:3000/login?error");
        }
    }

}
