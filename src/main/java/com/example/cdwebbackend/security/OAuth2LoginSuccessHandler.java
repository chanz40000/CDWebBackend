package com.example.cdwebbackend.security;

import com.example.cdwebbackend.components.JwtTokenUtil;
import com.example.cdwebbackend.dto.UserLoginDTO;
import com.example.cdwebbackend.entity.UserEntity;
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

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String googleId = oauth2User.getAttribute("sub");

        try {
            UserEntity userEntity = new UserEntity();
            userEntity.setEmail(email);
            userEntity.setGoogleAccountId(googleId);
            userEntity.setUsername(email);
            String token = jwtTokenUtil.generateToken(userEntity);

            if (token == null || token.isEmpty()) {
                response.sendRedirect("http://localhost:3000/login?error");
                return;
            }

            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 60 * 24); // Token hết hạn sau 1 ngày
            response.addCookie(cookie);

            // Log xem token có hợp lệ không
            System.out.println("OAuth2 login for email: " + email);

            System.out.println("Generated Token: " + token);
            response.sendRedirect("http://localhost:3000/home");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("http://localhost:3000/login?error");
        }
    }

}
