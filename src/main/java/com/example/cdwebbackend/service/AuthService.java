package com.example.cdwebbackend.service;

import com.example.cdwebbackend.security.CustomerOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService extends DefaultOAuth2UserService {
   @Value("${spring.security.oauth2.client.registration.google.client-id}")
     private String googleClientId;

   @Value("${spring.security.oauth2.client.registration.google.client-secret}")
   private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${spring.security.oauth2.client.registration.facebook.client-id}")
    private String facebookClientId;

    @Value("${spring.security.oauth2.client.registration.facebook.client-secret}")
    private String facebookClientSecret;

    @Value("${spring.security.oauth2.client.registration.facebook.redirect-uri}")
    private String facebookRedirectUri;



    public String generateAuthUrl(String loginType, HttpServletRequest request) {
        System.out.println("login with facebook");
        // Ví dụ đơn giản: Tạo URL login tùy theo loại mạng xã hội
        String redirectUri = request.getRequestURL().toString().replace("/social-login", "/callback");

        switch (loginType) {
            case "google":
                return "https://accounts.google.com/o/oauth2/auth?client_id="+ googleClientId
                        + "&redirect_uri=" + redirectUri
                        + "&response_type=code"
                        + "&scope=email profile";
            case "facebook":
                return "https://www.facebook.com/v17.0/dialog/oauth?client_id=" +facebookClientId
                        + "&redirect_uri=" + redirectUri
                        + "&response_type=code"
                        + "&scope=email,public_profile";
            default:
                throw new IllegalArgumentException("Unsupported login type: " + loginType);
        }
    }
    public String handleCallback(String loginType, String code) {
        // Giả lập xử lý lấy access token bằng mã code (thực tế sẽ gọi HTTP POST đến provider)
        switch (loginType) {
            case "google":
                return "Google access_token cho code: " + code;
            case "facebook":
                return "Facebook access_token cho code: " + code;
            default:
                throw new IllegalArgumentException("Unsupported login type: " + loginType);
        }
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return new CustomerOAuth2User(super.loadUser(userRequest)) ;
    }

    public Map<String, Object> authenticateAndFetchProfile(String code, String loginType) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> userInfo = new HashMap<>();

        if (loginType.equals("google")) {
            String tokenEndpoint = "https://oauth2.googleapis.com/token";
            String clientId = googleClientId;
            String clientSecret = googleClientSecret;
            String redirectUri = googleRedirectUri; // phải khớp với redirect_uri đã đăng ký

            // Step 1: Đổi code lấy access_token
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String body = "code=" + code +
                    "&client_id=" + clientId +
                    "&client_secret=" + clientSecret +
                    "&redirect_uri=" + redirectUri +
                    "&grant_type=authorization_code";

            HttpEntity<String> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    tokenEndpoint,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String accessToken = (String) response.getBody().get("access_token");

                // Step 2: Dùng access_token để lấy profile người dùng
                HttpHeaders profileHeaders = new HttpHeaders();
                profileHeaders.setBearerAuth(accessToken);

                HttpEntity<Void> profileRequest = new HttpEntity<>(profileHeaders);

                ResponseEntity<Map> profileResponse = restTemplate.exchange(
                        "https://www.googleapis.com/oauth2/v2/userinfo",
                        HttpMethod.GET,
                        profileRequest,
                        Map.class
                );

                if (profileResponse.getStatusCode() == HttpStatus.OK) {
                    userInfo = profileResponse.getBody();
                }
            }

        } else if (loginType.equals("facebook")) {
            // Viết tương tự như Google, đổi URL và tham số
            String tokenEndpoint = "https://graph.facebook.com/v17.0/oauth/access_token";
            String clientId = facebookClientId;
            String clientSecret = facebookClientSecret;
            String redirectUri = facebookRedirectUri;

            // Step 1: Đổi code lấy access_token
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String body = "code=" + code +
                    "&client_id=" + clientId +
                    "&client_secret=" + clientSecret +
                    "&redirect_uri=" + redirectUri;

            HttpEntity<String> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    tokenEndpoint,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String accessToken = (String) response.getBody().get("access_token");

                // Step 2: Dùng access_token để lấy profile người dùng
                HttpHeaders profileHeaders = new HttpHeaders();
                profileHeaders.setBearerAuth(accessToken);

                HttpEntity<Void> profileRequest = new HttpEntity<>(profileHeaders);

                ResponseEntity<Map> profileResponse = restTemplate.exchange(
                        "https://graph.facebook.com/me?fields=id,name,email",
                        HttpMethod.GET,
                        profileRequest,
                        Map.class
                );

                if (profileResponse.getStatusCode() == HttpStatus.OK) {
                    userInfo = profileResponse.getBody(); // ✅ userInfo sẽ có: id, name, email
                }
            }
            // Cần access_token → GET https://graph.facebook.com/me?fields=id,name,email&access_token=...
        }

        return userInfo.isEmpty() ? null : userInfo;
    }


}
