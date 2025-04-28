package com.example.cdwebbackend.config;

import com.example.cdwebbackend.entity.RoleEntity;
import com.example.cdwebbackend.filter.JwtTokenFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {
    @Autowired
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;

    public WebSecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    public static WebSecurityConfig createWebSecurityConfig(JwtTokenFilter jwtTokenFilter) {
        return new WebSecurityConfig(jwtTokenFilter);
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Áp dụng cho tất cả endpoint
                .allowedOrigins("https://localhost:3000") // Cho phép frontend React
                .allowedMethods("*") // GET, POST, PUT, DELETE, etc
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("Spring security dang kiem tra quyen truy cap");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(apiPrefix + "/users/register").permitAll()
                        .requestMatchers( apiPrefix+"/chat/send").permitAll()
                        .requestMatchers(apiPrefix + "/users/login").permitAll()
                        .requestMatchers(apiPrefix + "/products/getProduct?**").permitAll()
                        .requestMatchers(GET,apiPrefix + "/products/list").permitAll()
                        .requestMatchers(apiPrefix + "/users/upload-avatar").hasRole(RoleEntity.USER)
                        .requestMatchers(apiPrefix + "/products/upload-image").hasAnyRole(RoleEntity.ADMIN, RoleEntity.USER)
                        .requestMatchers(apiPrefix + "/products/add").hasAnyRole(RoleEntity.ADMIN)
                        .requestMatchers(apiPrefix + "/users/details").hasRole(RoleEntity.USER)
                        .requestMatchers(apiPrefix + "/users/changePassword?**").hasAnyRole(RoleEntity.USER, RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/users/details?**").hasAnyRole(RoleEntity.ADMIN, RoleEntity.USER)
                        .requestMatchers(POST, apiPrefix + "/orders").hasRole(RoleEntity.USER)
                        .requestMatchers(GET, apiPrefix + "/orders").hasAnyRole(RoleEntity.ADMIN, RoleEntity.USER)
                        .requestMatchers(DELETE, apiPrefix + "/orders").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/orders").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(GET, apiPrefix + "/v1/categories?**").hasAnyRole(RoleEntity.USER, RoleEntity.ADMIN)
                        .requestMatchers(POST, apiPrefix + "/v1/categories?**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(DELETE, apiPrefix + "/v1/categories?**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/categories?**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(GET, apiPrefix + "/categories?**").hasAnyRole(RoleEntity.USER, RoleEntity.ADMIN)
                        .requestMatchers(POST, apiPrefix + "/categories?**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(DELETE, apiPrefix + "/categories?**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/categories?**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(POST, apiPrefix + "/importOrder/**").hasRole(RoleEntity.ADMIN)

//                        .requestMatchers(GET, apiPrefix + "/products**").hasAnyRole(RoleEntity.USER, RoleEntity.ADMIN)
//                        .requestMatchers(POST, apiPrefix + "/products**").hasRole(RoleEntity.ADMIN)
//                        .requestMatchers(DELETE, apiPrefix + "/products**").hasRole(RoleEntity.ADMIN)
//                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/products**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(GET, apiPrefix + "/oder_detail**").hasAnyRole(RoleEntity.USER, RoleEntity.ADMIN)
                        .requestMatchers(POST, apiPrefix + "/oder_detail**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(DELETE, apiPrefix + "/oder_detail**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/oder_detail**").hasRole(RoleEntity.ADMIN)
                        .anyRequest().authenticated())
                .exceptionHandling(eh -> eh
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    System.out.println("Bị chặn bởi AccessDeniedHandler");
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: " + accessDeniedException.getMessage());
                })

        )

        ;
        return http.build();
    }
}
