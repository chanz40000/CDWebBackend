package com.example.cdwebbackend.service;

public interface IOnlinePaymentService {
    void updatePaymentStatus(String txnRef, Long id_status);
}
