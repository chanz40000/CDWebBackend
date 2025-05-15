package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.dto.BestSellingProductDTO;
import com.example.cdwebbackend.entity.OrderEntity;
import com.example.cdwebbackend.repository.OrderDetailRepository;
import com.example.cdwebbackend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    //tinh tong doanh thu theo ngay
    public double totalRevenueByDay(Date createDate){

        Calendar cal = Calendar.getInstance();
        cal.setTime(createDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date start = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 1); // ngày tiếp theo
        Date end = cal.getTime();

        List<OrderEntity> orderEntityList = orderRepository.findAllByCreatedDateBetween(start, end);
        System.out.println("list order: "+ orderEntityList);
        double total = 0;
        for (OrderEntity order: orderEntityList){
            total+= order.getTotalPrice();
        }
        System.out.println(createDate.toString()+": "+total);
        return total;
    }
    public double totalRevenueByDateBetween(Date createDate, Date endDate){

        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date start = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 1); // ngày tiếp theo
        Date end = cal.getTime();

        List<OrderEntity> orderEntityList = orderRepository.findAllByCreatedDateBetween(createDate, end);
        System.out.println("list order: "+ orderEntityList);
        double total = 0;
        for (OrderEntity order: orderEntityList){
            total+= order.getTotalPrice();
        }
        return total;
    }
    public double totalRevenueByMonth(Date startDate, Date endDate) {
        List<OrderEntity> orders = orderRepository.findAllByCreatedDateBetween(startDate, endDate);
        return orders.stream()
                .mapToDouble(OrderEntity::getTotalPrice)
                .sum();
    }
    public double totalRevenueByYear(int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        Date startDate = java.sql.Date.valueOf(start);
        Date endDate = java.sql.Date.valueOf(end);

        List<OrderEntity> orders = orderRepository.findAllByCreatedDateBetween(startDate, endDate);
        return orders.stream()
                .mapToDouble(OrderEntity::getTotalPrice)
                .sum();
    }

    public List<Double> listTotalRevenueByYear(int year) {
        List<Double> listRevenue = new ArrayList<>();

        // Loop through each month (1 to 12)
        for (int month = 1; month <= 12; month++) {
            // Set start and end dates for the month
            LocalDate start = LocalDate.of(year, month, 1);
            LocalDate end = start.withDayOfMonth(start.lengthOfMonth()); // Last day of the month

            // Convert to java.sql.Date
            Date startDate = java.sql.Date.valueOf(start);
            Date endDate = java.sql.Date.valueOf(end);

            // Calculate revenue for the month using the existing totalRevenueByMonth method
            double monthlyRevenue = totalRevenueByMonth(startDate, endDate);
            listRevenue.add(monthlyRevenue);
        }

        return listRevenue;
    }
    public Map<Date, Double> totalRevenueByDateRange(Date startDate, Date endDate) {

        // Tạo map để lưu doanh thu theo ngày
        Map<Date, Double> revenueByDate = new TreeMap<>();
        // Lặp qua từng ngày trong khoảng
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        Date currentDate = new Date(startDate.getTime());

        while (!currentDate.after(endDate)) {

            calendar.add(Calendar.DAY_OF_MONTH, 1);
            currentDate = calendar.getTime();
            double revenue = totalRevenueByDay(currentDate);
            revenueByDate.put(new Date(currentDate.getTime()),revenue);
        }

        return revenueByDate;
    }
    public List<BestSellingProductDTO> getTop10BestSellingProducts() {
        List<Object[]> results = orderDetailRepository.findTop10BestSellingProducts();
        return results.stream()
                .map(result -> new BestSellingProductDTO(
                        (Long) result[0],                          // productId
                        (String) result[1],                        // productName
                        ((BigDecimal) result[2]).longValue()       // totalQuantity
                ))
                .collect(Collectors.toList());
    }
}
