//package com.example.cdwebbackend.config;
//
//import com.example.cdwebbackend.components.JwtTokenUtil;
//import com.example.cdwebbackend.entity.UserEntity;
//import com.example.cdwebbackend.filter.JwtTokenFilter;
//import com.example.cdwebbackend.repository.UserRepository;
//import com.example.cdwebbackend.service.impl.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import lombok.RequiredArgsConstructor;
//
//@Configuration
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private AuthenticationConfiguration configuration;
//    @Autowired
//    private final UserDetailsService userDetailsService;
//    @Autowired
//    private final JwtTokenUtil jwtTokenUtil;
//
//
//
//    //user's detail object
//    @Bean
//    public UserDetailsService userDetailsService(){
//        return username ->{
//            return userRepository
//                    .findOneByUsername(username)
//                    .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with username ="+ username));
//        };
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//    @Bean
//    public AuthenticationProvider authenticationProvider(){
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService());
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){
//        this.configuration = configuration;
//        try {
//            return configuration.getAuthenticationManager();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//}
package com.example.cdwebbackend.config;

import com.example.cdwebbackend.components.JwtTokenUtil;
import com.example.cdwebbackend.dto.UserLoginDTO;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.filter.JwtTokenFilter;
import com.example.cdwebbackend.repository.UserRepository;
import com.example.cdwebbackend.security.CustomOAuth2UserService;
import com.example.cdwebbackend.security.OAuth2LoginSuccessHandler;
import com.example.cdwebbackend.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig{
    private final UserRepository userRepository;
    private final AuthenticationConfiguration configuration;
    private final JwtTokenFilter jwtFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;


    // UserDetailsService bean để load thông tin user từ cơ sở dữ liệu
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> (UserDetails) userRepository
                .findOneByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with username =" + username));
    }

    // PasswordEncoder bean để mã hóa mật khẩu người dùng
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationProvider bean sử dụng UserDetailsService và PasswordEncoder để xác thực
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // AuthenticationManager bean dùng để quản lý và xác thực người dùng
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Autowired
    CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        System.out.println("⚙️ SecurityFilterChain đang được cấu hình!");

        return http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable()) // Tắt CSRF cho REST API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/users/login",
                                "/api/v1/users/register",
                                "/api/v1/products/list",
                                "/oauth2/**",
                                "/login/oauth2/**",
                                "/auth/social-login",
                                "/auth/social/callback",
                                "/oauth2/authorization/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(user -> user
                                .userService(customOAuth2UserService) // ✨ BẮT BUỘC PHẢI CÓ
                        )
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler((request, response, exception) -> {
                            exception.printStackTrace(); // In lỗi để debug
                            response.sendRedirect("http://localhost:3000/login?error=oauth2_failed");
                        })
                )

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/auth/social-login", "/auth/social/callback").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .oauth2Login()
//                .loginPage("/auth/social-login");
//    }
}
