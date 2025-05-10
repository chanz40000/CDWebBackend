////package com.example.cdwebbackend.config;
////
////import com.example.cdwebbackend.components.JwtTokenUtil;
////import com.example.cdwebbackend.entity.UserEntity;
////import com.example.cdwebbackend.filter.JwtTokenFilter;
////import com.example.cdwebbackend.repository.UserRepository;
////import com.example.cdwebbackend.service.impl.UserService;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.security.authentication.AuthenticationManager;
////import org.springframework.security.authentication.AuthenticationProvider;
////import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
////import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
////import org.springframework.security.core.userdetails.UserDetailsService;
////import org.springframework.security.core.userdetails.UsernameNotFoundException;
////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
////import org.springframework.security.crypto.password.PasswordEncoder;
////import lombok.RequiredArgsConstructor;
////
////@Configuration
////@RequiredArgsConstructor
////public class SecurityConfig {
////
////    @Autowired
////    private UserRepository userRepository;
////
////    @Autowired
////    private AuthenticationConfiguration configuration;
////    @Autowired
////    private final UserDetailsService userDetailsService;
////    @Autowired
////    private final JwtTokenUtil jwtTokenUtil;
////
////
////
////    //user's detail object
////    @Bean
////    public UserDetailsService userDetailsService(){
////        return username ->{
////            return userRepository
////                    .findOneByUsername(username)
////                    .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with username ="+ username));
////        };
////    }
////    @Bean
////    public PasswordEncoder passwordEncoder(){
////        return new BCryptPasswordEncoder();
////    }
////    @Bean
////    public AuthenticationProvider authenticationProvider(){
////        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
////        authenticationProvider.setUserDetailsService(userDetailsService());
////        authenticationProvider.setPasswordEncoder(passwordEncoder());
////        return authenticationProvider;
////    }
////    @Bean
////    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){
////        this.configuration = configuration;
////        try {
////            return configuration.getAuthenticationManager();
////        } catch (Exception e) {
////            throw new RuntimeException(e);
////        }
////
////    }
////
////}
//package com.example.cdwebbackend.config;
//
//import com.example.cdwebbackend.components.JwtTokenUtil;
//import com.example.cdwebbackend.dto.UserLoginDTO;
//import com.example.cdwebbackend.entity.UserEntity;
//import com.example.cdwebbackend.filter.JwtTokenFilter;
//import com.example.cdwebbackend.repository.UserRepository;
//import com.example.cdwebbackend.security.CustomOAuth2UserService;
//import com.example.cdwebbackend.security.OAuth2LoginSuccessHandler;
//import com.example.cdwebbackend.service.impl.UserService;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@RequiredArgsConstructor
//@EnableWebSecurity
//public class SecurityConfig{
//    private final UserRepository userRepository;
//    private final AuthenticationConfiguration configuration;
//    private final JwtTokenFilter jwtFilter;
//    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
//
//
//    // UserDetailsService bean để load thông tin user từ cơ sở dữ liệu
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> (UserDetails) userRepository
//                .findOneByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with username =" + username));
//    }
//
//    // PasswordEncoder bean để mã hóa mật khẩu người dùng
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    // AuthenticationProvider bean sử dụng UserDetailsService và PasswordEncoder để xác thực
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService());
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }
//
//    // AuthenticationManager bean dùng để quản lý và xác thực người dùng
//    @Bean
//    public AuthenticationManager authenticationManager() throws Exception {
//        return configuration.getAuthenticationManager();
//    }
//
//    @Autowired
//    CustomOAuth2UserService customOAuth2UserService;
//
//    @Bean
//    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
//        System.out.println("⚙️ SecurityFilterChain đang được cấu hình!");
//
//        return
//        http
//                .cors(cors -> cors.configurationSource(request -> {
//                    CorsConfiguration config = new CorsConfiguration();
//                    config.setAllowedOrigins(Arrays.asList("https://localhost:3000"));
//                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
//                    config.setAllowedHeaders(Arrays.asList("*"));
//                    config.setAllowCredentials(true);
//                    return config;
//                })) // Cho phép CORS
//                .csrf(csrf -> csrf.disable()) // Tắt CSRF cho REST API
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/api/v1/users/login",
//                                "/api/v1/users/register",
//                                "/api/v1/users/forgotPass",
//                                "/api/v1/products/getProduct/**",
//                                "/api/v1/products/list",
//                                "/api/v1/ghn/districts",
//                                "/oauth2/**",
//                                "/login/oauth2/**",
//                                "/auth/social-login",
//                                "/auth/social/callback",
//                                "/oauth2/authorization/**",
//                                "/api/chat/send"
//                        ).permitAll()
//                        .anyRequest().authenticated()
//                )
//
//                .exceptionHandling(exception -> exception
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                            response.setContentType("application/json");
//                            response.getWriter().write("{\"error\": \"Unauthorized\"}");
//                        })
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        .userInfoEndpoint(user -> user
//                                .userService(customOAuth2UserService)
//                        )
//                        .successHandler(oAuth2LoginSuccessHandler)
//                        .failureHandler((request, response, exception) -> {
//                            exception.printStackTrace(); // In lỗi để debug
//                            response.sendRedirect("https://localhost:3000/login?error=oauth2_failed");
//                        })
//                )
//
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("https://localhost:3000"));
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
//        config.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
//
//
//
////    protected void configure(HttpSecurity http) throws Exception {
////        http
////                .authorizeRequests()
////                .antMatchers("/auth/social-login", "/auth/social/callback").permitAll()
////                .anyRequest().authenticated()
////                .and()
////                .oauth2Login()
////                .loginPage("/auth/social-login");
////    }
//}
package com.example.cdwebbackend.config;

import com.example.cdwebbackend.entity.RoleEntity;
import com.example.cdwebbackend.filter.JwtTokenFilter;
import com.example.cdwebbackend.repository.UserRepository;
import com.example.cdwebbackend.security.CustomOAuth2UserService;
import com.example.cdwebbackend.security.OAuth2LoginSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final AuthenticationConfiguration configuration;
    private final JwtTokenFilter jwtFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository
                .findOneByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with username =" + username));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("https://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        System.out.println("⚙️ SecurityFilterChain đang được cấu hình!");

        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/error",
                                "/login",
                                apiPrefix + "/users/login",
                                apiPrefix + "/users/register",
                                apiPrefix + "/users/forgotPass",
                                apiPrefix + "/users/newPassword/**",
                                apiPrefix + "/users/validateOtp",
                                apiPrefix + "/products/getProduct/**",
                                apiPrefix + "/products/getProductName/**",
                                apiPrefix + "/products/getListColor/**",
                                apiPrefix + "/products/getIdProductSizeColor/**",
                                apiPrefix + "/products/getListSize/**",
                                apiPrefix + "/products/list",
                                apiPrefix + "/products/list_page",
                                apiPrefix + "/ghn/districts",
                                apiPrefix + "/chat/send",
                                apiPrefix + "/payments/ipn",
                                apiPrefix + "/payments/return",
                                // quyenf admin
                                apiPrefix + "/products/getCategory/**",
                                apiPrefix + "/products/getBrand/**",
                                apiPrefix + "/products/getSize/**",
                                apiPrefix + "/products/getColor/**",
                                apiPrefix + "/products/getColorProduct/**",
                                apiPrefix + "/products/getProductSizeColor/**",
                                "/oauth2/**",
                                "/login/oauth2/**",
                                "/auth/social-login",
                                "/auth/social/callback",
                                "/oauth2/authorization/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, apiPrefix + "/importOrder/**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.POST, apiPrefix + "/importOrder/insert").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.GET, apiPrefix + "/importOrder/list").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.POST, apiPrefix + "/orders").hasRole(RoleEntity.USER)
                        .requestMatchers(HttpMethod.GET, apiPrefix + "/orders").hasAnyRole(RoleEntity.ADMIN, RoleEntity.USER)
                        .requestMatchers(HttpMethod.DELETE, apiPrefix + "/orders").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/orders").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.GET, apiPrefix + "/v1/categories/**").hasAnyRole(RoleEntity.USER, RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.POST, apiPrefix + "/v1/categories/**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, apiPrefix + "/v1/categories/**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/categories/**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.GET, apiPrefix + "/oder_detail/**").hasAnyRole(RoleEntity.USER, RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.POST, apiPrefix + "/oder_detail/**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, apiPrefix + "/oder_detail/**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/oder_detail/**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(apiPrefix + "/users/upload-avatar").hasAnyRole(RoleEntity.USER, RoleEntity.ADMIN)
                        .requestMatchers(apiPrefix + "/products/upload-image").hasAnyRole(RoleEntity.ADMIN, RoleEntity.USER)

                        .requestMatchers(apiPrefix + "/products/add").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(apiPrefix + "/users/details").hasAnyRole(RoleEntity.USER, RoleEntity.ADMIN)

                        .requestMatchers(apiPrefix + "/orders/prepare").hasRole(RoleEntity.USER)
                        .requestMatchers(apiPrefix + "/orders/add-shipping-address").hasRole(RoleEntity.USER)
                        .requestMatchers(apiPrefix + "/carts/**").hasRole(RoleEntity.USER)
                        .requestMatchers(apiPrefix + "/payments/create-payment").hasRole(RoleEntity.USER)
                        .requestMatchers(apiPrefix + "/users/changePassword/**").hasAnyRole(RoleEntity.USER, RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/users/details/**").hasAnyRole(RoleEntity.ADMIN, RoleEntity.USER)
                        .anyRequest().authenticated()
                )
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println("Yêu cầu bị từ chối do chưa xác thực: " + authException.getMessage());
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthorized: " + authException.getMessage() + "\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            System.out.println("Yêu cầu bị từ chối do thiếu quyền: " + accessDeniedException.getMessage());
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Access Denied: " + accessDeniedException.getMessage() + "\"}");
                        })
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(user -> user.userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler((request, response, exception) -> {
                            System.out.println("OAuth2 login thất bại: " + exception.getMessage());
                            exception.printStackTrace();
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"OAuth2 login failed: " + exception.getMessage() + "\"}");
                        })
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}