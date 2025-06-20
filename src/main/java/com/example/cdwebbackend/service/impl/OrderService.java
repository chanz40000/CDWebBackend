package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.dto.BestSellingProductDTO;
import com.example.cdwebbackend.entity.OrderEntity;
import com.example.cdwebbackend.entity.OrderReasonEntity;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.example.cdwebbackend.repository.*;
import com.example.cdwebbackend.converter.OrderConverter;
import com.example.cdwebbackend.dto.OrderDTO;
import com.example.cdwebbackend.entity.OrderEntity;
import com.example.cdwebbackend.repository.OrderRepository;
import com.example.cdwebbackend.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    StatusOrderRepository statusOrderRepository;
    @Autowired
    OrderReasonRepository orderReasonRepository;

    private List<Transaction> transactions; // Danh sách giao dịch
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

    public Map<Integer, Double> totalRevenueByMonthInYear(int year) {
        // Tạo map để lưu doanh thu theo tháng
        Map<Integer, Double> revenueByMonth = new TreeMap<>();

        // Lấy tháng hiện tại
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH bắt đầu từ 0

        // Xác định số tháng cần tính (nếu năm là hiện tại thì chỉ tính đến tháng hiện tại)
        int maxMonth = (year == currentYear) ? currentMonth : 12;

        // Lặp qua từng tháng đã qua trong năm
        for (int month = 1; month <= maxMonth; month++) {
            // Tính ngày đầu và cuối của tháng
            Calendar startCal = Calendar.getInstance();
            startCal.set(year, month - 1, 1, 0, 0, 0); // Ngày đầu tháng
            startCal.set(Calendar.MILLISECOND, 0);

            Calendar endCal = Calendar.getInstance();
            endCal.set(year, month - 1, startCal.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59); // Ngày cuối tháng
            endCal.set(Calendar.MILLISECOND, 999);

            // Tính doanh thu cho tháng hiện tại
            double revenue = totalRevenueByMonth(startCal.getTime(), endCal.getTime());
            revenueByMonth.put(month, revenue);
        }

        return revenueByMonth;
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



    @Autowired
    private OrderConverter orderConverter;

    @Override
    public List<OrderDTO> getAllOrdersByUserId(Long userId) {
        List<OrderDTO> orderDTOS = new ArrayList<>();

        List<OrderEntity> orderEntities = orderRepository.findByUserId(userId);
        for (OrderEntity orderEntity : orderEntities) {
            orderDTOS.add(orderConverter.toDTO(orderEntity));
        }
        return orderDTOS;
    }

    @Override
    public OrderDTO getOrdersByIdAndUserId(Long userId, Long orderId) {
        OrderEntity orderEntity = orderRepository.findByUserIdAndId(userId, orderId);
        return orderConverter.toDTO(orderEntity);
    }

    public OrderDTO getOrdersById(Long orderId) throws DataNotFoundException {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));;
        return orderConverter.toDTO(orderEntity);
    }

    @Override
    public List<OrderDTO> getOrdersByUserIdAndStatusId(Long userId, Long statusId) {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        List<OrderEntity> orderEntities = orderRepository.findByUserIdAndStatusOrderId(userId, statusId);
        for (OrderEntity orderEntity : orderEntities) {
            orderDTOS.add(orderConverter.toDTO(orderEntity));
        }
        return orderDTOS;
    }

    //cap nhat lai status order khi sau 1 ngay khong thanh toan online
    public void updateOverdueOrders() {
        // Tính thời điểm cách đây 1 ngày
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date oneDayAgo = calendar.getTime();
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MINUTE, -1);
//        Date oneMinuteAgo = calendar.getTime();


        // Tìm tất cả đơn hàng có status_order_id = 7 và sửa đổi trước 1 ngày
        List<OrderEntity> overdueOrders = orderRepository.findByStatusOrderIdAndModifiedDateBefore(7L, oneDayAgo);

        // Cập nhật trạng thái sang 6 đã hủy cho các đơn hàng quá hạn + lý do hủy
        for (OrderEntity order : overdueOrders) {
            OrderReasonEntity reason = orderReasonRepository.findOneById(24);
            String reason_order = reason.getReasonGroup()+": "+reason.getReason();
            order.setCancelReason(reason_order);
            order.setStatusOrder(statusOrderRepository.findOneById(6));
            order.setModifiedDate(new Date()); // Cập nhật thời gian sửa đổi
            orderRepository.save(order);
        }
    }

}
