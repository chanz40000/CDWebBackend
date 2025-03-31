package com.example.cdwebbackend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example.cdwebbackend")
@EnableJpaRepositories(basePackages = "com.example.cdwebbackend.repository")

public class CdWebBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CdWebBackendApplication.class, args);
    }
}
