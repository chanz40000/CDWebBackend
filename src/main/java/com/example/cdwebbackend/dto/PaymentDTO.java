package com.example.cdwebbackend.dto;

public class PaymentDTO extends AbstractDTO<PaymentDTO> {
    private String method;

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
}
