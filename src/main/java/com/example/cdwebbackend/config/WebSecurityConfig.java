package com.example.cdwebbackend.config;

import com.example.cdwebbackend.entity.RoleEntity;
import com.example.cdwebbackend.filter.JwtTokenFilter;
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

import static org.springframework.http.HttpMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(apiPrefix + "/users/register").permitAll()
                        .requestMatchers(apiPrefix + "/users/login").permitAll()
                        .requestMatchers(apiPrefix + "/users/details").permitAll()
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
                        .requestMatchers(GET, apiPrefix + "/products**").hasAnyRole(RoleEntity.USER, RoleEntity.ADMIN)
                        .requestMatchers(POST, apiPrefix + "/products**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(DELETE, apiPrefix + "/products**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/products**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(GET, apiPrefix + "/oder_detail**").hasAnyRole(RoleEntity.USER, RoleEntity.ADMIN)
                        .requestMatchers(POST, apiPrefix + "/oder_detail**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(DELETE, apiPrefix + "/oder_detail**").hasRole(RoleEntity.ADMIN)
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/oder_detail**").hasRole(RoleEntity.ADMIN)



                        .anyRequest().authenticated());
        return http.build();
    }
}
