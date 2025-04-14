package com.example.cdwebbackend;
import com.example.cdwebbackend.service.impl.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example.cdwebbackend")
@EnableJpaRepositories(basePackages = "com.example.cdwebbackend.repository")

public class CdWebBackendApplication {
    @Autowired
    private EmailSenderService senderService;

    public static void main(String[] args) {
        SpringApplication.run(CdWebBackendApplication.class, args);
    }
    @EventListener(ApplicationReadyEvent.class)
    public void sendMail(){
        senderService.sendEmail("ntchanz4000@gmail.com",
                "This is test email",
                "this is body of email");
    }
}
