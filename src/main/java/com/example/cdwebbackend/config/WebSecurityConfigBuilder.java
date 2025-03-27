package com.example.cdwebbackend.config;

import com.example.cdwebbackend.filter.JwtTokenFilter;

public class WebSecurityConfigBuilder {
    private JwtTokenFilter jwtTokenFilter;

    public WebSecurityConfigBuilder setJwtTokenFilter(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
        return this;
    }

    public WebSecurityConfig createWebSecurityConfig() {
        return WebSecurityConfig.createWebSecurityConfig(jwtTokenFilter);
    }
}