package com.example.cdwebbackend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component

public class ApiProperties {
    private final String prefix="/api/v1";
}
