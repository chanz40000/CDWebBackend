package com.example.cdwebbackend.config;

import com.example.cdwebbackend.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusScheduler {

    @Autowired
    private OrderService orderService;

    // Chạy mỗi giờ (có thể điều chỉnh biểu thức cron)
  @Scheduled(cron = "0 0 * * * ?") // Chạy vào đầu mỗi giờ
//    @Scheduled(cron = "*/10 * * * * ?") // Mỗi 10 giây

    public void checkAndUpdateOrderStatus() {
        orderService.updateOverdueOrders();
    }
}