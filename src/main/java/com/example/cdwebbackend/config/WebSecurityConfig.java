package com.example.cdwebbackend.config;

import com.example.cdwebbackend.filter.JwtTokenFilter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
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
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value(staticConstructor = "${api.prefix}")
    private String apiPrefix;


    private WebSecurityConfig(JwtTokenFilter jwtTokenFilter) {
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
                        .requestMatchers(String.format("%s/users/register", apiPrefix)).permitAll()
                        .requestMatchers(POST, String.format("%s/orders", apiPrefix)).hasRole("USER")
                        .requestMatchers(GET, String.format("%s/orders", apiPrefix)).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(DELETE, String.format("%s/orders", apiPrefix)).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, String.format("%s/orders", apiPrefix)).hasRole("ADMIN")
                        .anyRequest().authenticated());
        return http.build();
    }
}
