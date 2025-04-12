package com.example.cdwebbackend.payload;

import org.springframework.http.HttpStatus;

public class ResponseObject {
    private String status;
    private String message;
    private Object data;

    public ResponseObject(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Getters and setters (hoặc dùng Lombok @Data)
}
