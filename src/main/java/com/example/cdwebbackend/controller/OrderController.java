package com.example.cdwebbackend.controller;

import com.example.cdwebbackend.converter.CartItemConverter;
import com.example.cdwebbackend.converter.OrderConverter;
import com.example.cdwebbackend.converter.ShippingAddressConverter;
import com.example.cdwebbackend.dto.*;
import com.example.cdwebbackend.entity.*;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.example.cdwebbackend.repository.*;
import com.example.cdwebbackend.responses.*;
import com.example.cdwebbackend.service.ICartItemService;
import com.example.cdwebbackend.service.IOrderService;
import com.example.cdwebbackend.service.IShippingAddressService;
import com.example.cdwebbackend.service.impl.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ICartItemService cartItemService;

    @Autowired
    private CartItemConverter cartItemConverter;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private IShippingAddressService shippingAddressService;

    @Autowired
    private ShippingAddressConverter shippingAddressConverter;
    @Autowired
    private ShippingAddressRePository shippingAddressRePository;

    @Autowired
    private StatusOrderRepository statusOrderRepository;

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    OrderService orderService;

//    @Autowired
//    private IOrderService orderService;

    @Autowired
    private OrderConverter orderConverter;

//    @Autowired
//    priva

    @PostMapping("/prepare")
    public ResponseEntity<?> prepareOrder(
            @RequestParam("cart_item_id")List<Long> cartItemIds){

        try{
            // Lấy user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserEntity user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));

            //lay cart
            CartEntity cart = cartRepository.findByUserId(user.getId())
                    .orElseGet(() -> {
                        CartEntity cartEntity = new CartEntity();
                        cartEntity.setUser(user);
                        return cartRepository.save(cartEntity);
                    });

            //Lay thong tin cartItems được chọn
            List<CartItemEntity> selectedItems = cartItemService.getCartItemsByIds(cartItemIds, user.getId());
            // Kiểm tra nếu không có cart item nào hợp lệ
            if (selectedItems.isEmpty()) {
                return ResponseEntity.ok().body(
                        java.util.Map.of(
                                "status", "failed",
                                "message", "Không tìm thấy các sản phẩm trong giỏ hàng"
                        )
                );
            }
            // tin tong tien
            int totalPrice = selectedItems.stream()
                    .mapToInt(item -> item.getQuantity() * item.getProductSizeColor().getProduct().getPrice())
                    .sum();

            // tinh tong so luong
            int totalQuantity = selectedItems.stream()
                    .mapToInt(CartItemEntity::getQuantity)
                    .sum();

            //Mapping sang DTO
            List<CartItemDTO> itemDTOS = selectedItems.stream()
                    .map(cartItemConverter::toDTO)
                    .collect(Collectors.toList());
            List<CartItemResponse> cartItemResponses = itemDTOS.stream()
                    .map(dto -> CartItemResponse.fromEntity(cartItemConverter.toEntity(dto, cart)))
                    .collect(Collectors.toList());


            PrepareOrderDTO prepareOrderDTO = new PrepareOrderDTO();
//            prepareOrderDTO.setId(user.getId());
            prepareOrderDTO.setTotalPrice(totalPrice);
            prepareOrderDTO.setTotalQuantity(totalQuantity);
            prepareOrderDTO.setCartItems(cartItemResponses);

            return ResponseEntity.ok().body(
                    java.util.Map.of(
                            "status", "success",
                            "message", "Đơn hàng đã tạm thời tạo",
                            "data", prepareOrderDTO
                    )
            );


        }catch (Exception e){
            return ResponseEntity.badRequest().body(
                    java.util.Map.of(
                            "status", "failed",
                            "message", e.getMessage()
                    )
            );
        }
    }

@Transactional
@PostMapping("/add-order")
public ResponseEntity<?> addOrder(
        @RequestParam("cartItemIds") List<Long> cartItemIds,
        @RequestParam("shippingAddressId") Long shippingAddressId,
        @RequestParam("paymentId") Long paymentId,
        @RequestParam("shippingFee") int shippingFee,
        @RequestParam("finalPrice") int finalPrice,
        @RequestParam("totalPrice") int totalPrice,
        @RequestParam("note") String note
) {
    try {
        // Log dữ liệu đầu vào để debug
        System.out.println("Received request for /add-order:");
        System.out.println("cartItemIds: " + cartItemIds);
        System.out.println("shippingAddressId: " + shippingAddressId);
        System.out.println("paymentId: " + paymentId);
        System.out.println("shippingFee: " + shippingFee);
        System.out.println("finalPrice: " + finalPrice);
        System.out.println("totalPrice: " + totalPrice);
        System.out.println("note: " + note);

        // Kiểm tra dữ liệu đầu vào
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed",
                    "message", "Danh sách cartItemIds không được rỗng"
            ));
        }
        if (shippingAddressId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed",
                    "message", "shippingAddressId không được rỗng"
            ));
        }
        if (paymentId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed",
                    "message", "paymentId không được rỗng"
            ));
        }
        if (finalPrice <= 0 || totalPrice <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed",
                    "message", "finalPrice và totalPrice phải lớn hơn 0"
            ));
        }

        // Lấy user từ authentication context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

//        // Lấy địa chỉ giao hàng từ shippingAddressId
//        ShippingAddressEntity shippingAddress = shippingAddressRePository.findById(shippingAddressId)
//                .orElseThrow(() -> new DataNotFoundException("Địa chỉ giao hàng không hợp lệ với ID: " + shippingAddressId));


        ShippingAddressEntity shippingAddressEntity = shippingAddressRePository.findByIdAndUserId(shippingAddressId, user.getId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy địa chỉ"));


        // Tạo đơn hàng
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setReceiverName(shippingAddressEntity.getReceiverName());
        order.setReceiverPhone(shippingAddressEntity.getReceiverPhone());
        order.setProvince(shippingAddressEntity.getProvince());
        order.setDistrict(shippingAddressEntity.getDistrict());
        order.setWard(shippingAddressEntity.getWard());
        order.setAddressDetail(shippingAddressEntity.getAddressDetail());
        order.setShippingFee(shippingFee);
        order.setFinalPrice(finalPrice);
        order.setTotalPrice(totalPrice);
        order.setNote(note);

        // Thiết lập trạng thái đơn hàng dựa trên paymentId
        Long statusOrderId = (paymentId == 3) ? 7L : 1L; // 2: Chờ thanh toán (VNPay), 1: Chờ xác nhận (COD, Momo)
        StatusOrderEntity statusOrder = statusOrderRepository.findById(statusOrderId)
                .orElseThrow(() -> new DataNotFoundException("Trạng thái đơn hàng không hợp lệ với ID: " + statusOrderId));
        order.setStatusOrder(statusOrder);

        // Lấy phương thức thanh toán
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new DataNotFoundException("Phương thức thanh toán không hợp lệ với ID: " + paymentId));
        order.setPayment(payment);

        // Lưu đơn hàng
        order = orderRepository.save(order);

        // Lưu OrderDetail
        List<OrderDetailEntity> orderDetailEntities = new ArrayList<>();
        for (Long cartItemId : cartItemIds) {
            CartItemEntity cartItem = cartItemRepository.findByIdAndUserId(cartItemId, user.getId());
            if (cartItem == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "status", "failed",
                        "message", "Cart item ID " + cartItemId + " không tồn tại hoặc không thuộc về user"
                ));
            }

            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setProductSizeColor(cartItem.getProductSizeColor());
            orderDetail.setPriceUnit(cartItem.getProductSizeColor().getProduct().getPrice());
            orderDetail.setSubtotal(cartItem.getQuantity() * cartItem.getProductSizeColor().getProduct().getPrice());
            orderDetailEntities.add(orderDetail);
        }
        order.setOrderDetails(orderDetailEntities);
        orderRepository.save(order);

        // Chỉ xóa cartItem nếu không phải VNPay (paymentId != 3)
//        if (paymentId != 3) {
//
//        }
        cartItemRepository.deleteByIdInAndUserId(cartItemIds, user.getId());

        // Convert sang OrderResponse
        OrderResponse orderResponse = OrderResponse.fromEntity(order);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Đơn hàng đã được tạo thành công",
                "data", orderResponse
        ));

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", "failed",
                "message", "Lỗi khi tạo đơn hàng: " + e.getMessage()
        ));
    }
}
    @GetMapping("/getOrder")
    public ResponseEntity<?> getOrderByUserId(
            @RequestParam("orderId") Long orderId
    ) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            UserEntity user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));


            OrderDTO orderDTOS = orderService.getOrdersByIdAndUserId(user.getId(), orderId);

            OrderResponse response = OrderResponse.fromEntity(orderConverter.toEntity(orderDTOS));
            System.out.println(response.toString()+ "/"+ response.getTotalPrice());


            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", response
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/getOrder_byUser")
    public ResponseEntity<?> getOrderByUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();


            UserEntity user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));

            List<OrderDTO> orderDTOS = orderService.getAllOrdersByUserId(user.getId());

            // In ra từng OrderDTO
            for (OrderDTO dto : orderDTOS) {
                System.out.println("OrderDTO: " + dto);
            }

            List<OrderResponse> responses = orderDTOS.stream()
                    .map(orderDTO -> OrderResponse.fromEntity(orderConverter.toEntity(orderDTO)))
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("orders", responses);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getOrder/status")
    public ResponseEntity<?> getOrderByStatus(
            @RequestParam("statusId") Long statusId
    ) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();


            UserEntity user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));

            List<OrderDTO> orderDTOS = orderService.getOrdersByUserIdAndStatusId(user.getId(),statusId);

            // In ra từng OrderDTO
            for (OrderDTO dto : orderDTOS) {
                System.out.println("OrderDTO: " + dto);
            }

            List<OrderResponse> responses = orderDTOS.stream()
                    .map(orderDTO -> OrderResponse.fromEntity(orderConverter.toEntity(orderDTO)))
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("orders", responses);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/add-shipping-address")
    public ResponseEntity<?> addShippingAddress(@RequestParam("receiver_name") String receiver_name,
                                                @RequestParam("receiver_phone") String receiver_phone,
                                                @RequestParam("province") String province,
                                                @RequestParam("district") String district,
                                                @RequestParam("ward") String ward,
                                                @RequestParam("address_detail") String address_detail){

        try{
            // Lấy thông tin người dùng từ token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Lấy user và cart tương ứng
            UserEntity user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));

            ShippingAddressDTO shippingAddressDTO = new ShippingAddressDTO();
            shippingAddressDTO.setUser(user.getId());
            shippingAddressDTO.setReceiverName(receiver_name);
            shippingAddressDTO.setReceiverPhone(receiver_phone);
            shippingAddressDTO.setProvince(province);
            shippingAddressDTO.setDistrict(district);
            shippingAddressDTO.setWard(ward);
            shippingAddressDTO.setAddressDetail(address_detail);
            ShippingAddressEntity newshippingAddressDTO = shippingAddressService.addShippingAddress(user.getId(),shippingAddressDTO);
            ShippingAddressResponse response = ShippingAddressResponse.fromEntity(newshippingAddressDTO);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Thêm địa chỉ thành công",
                    "data", response
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed",
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/get-shipping-address")
    public ResponseEntity<?> getListShippingAddressUser(){
        try{
            // Lấy thông tin người dùng từ SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Tìm user theo username
            UserEntity user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));


            List<ShippingAddressEntity> addressEntities = shippingAddressService.getAllShippingAddress(user.getId());

            List<ShippingAddressResponse> addressResponses = addressEntities.stream()
                    .map(ShippingAddressResponse::fromEntity)
                    .toList();

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", addressResponses.isEmpty() ?
                            "Chưa có địa chỉ giao hàng nào" :
                            "Lấy danh sách địa chỉ giao hàng thành công",
                    "data", addressResponses
            ));

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "failed",
                    "message", "Đã xảy ra lỗi khi lấy danh sách địa chỉ giao hàng"
            ));
        }

    }

    @PutMapping("/choose-shipping-address")
    public ResponseEntity<?> chooseShippingAddress(@RequestParam("addressId") Long addressId){

        try{
            // Lấy người dùng từ token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Tìm user theo username
            UserEntity user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));

            shippingAddressService.chooseShippingAddress(user.getId(), addressId);

            // Lấy lại danh sách địa chỉ sau khi cập nhật
            List<ShippingAddressEntity> addressEntities = shippingAddressService.getAllShippingAddress(user.getId());

            // Convert sang response
            List<ShippingAddressResponse> addressResponses = addressEntities.stream()
                    .map(ShippingAddressResponse::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Chọn địa chỉ giao hàng thành công",
                    "data", addressResponses
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed",
                    "message", e.getMessage()
            ));
        }

    }

    @GetMapping("/get-selected-shipping-address")
    public ResponseEntity<?> getSelectedShippingAddress() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            UserEntity user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));

            ShippingAddressEntity selectedAddress = shippingAddressService.getSelectedAddress(user.getId());

            if (selectedAddress == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "status", "failed",
                        "message", "Người dùng chưa chọn địa chỉ giao hàng nào"
                ));
            }

            ShippingAddressResponse response = ShippingAddressResponse.fromEntity(selectedAddress);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Lấy địa chỉ giao hàng đã chọn thành công",
                    "data", response
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed",
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/edit-shipping-address")
    public ResponseEntity<?> editShippingAddress(
                                                @RequestParam("id") Long id,
                                                @RequestParam("receiver_name") String receiver_name,
                                                @RequestParam("receiver_phone") String receiver_phone,
                                                @RequestParam("province") String province,
                                                @RequestParam("district") String district,
                                                @RequestParam("ward") String ward,
                                                @RequestParam("address_detail") String address_detail){

        try{
            // Lấy thông tin người dùng từ token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Lấy user và cart tương ứng
            UserEntity user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));

            // tạo moi thong tin
            ShippingAddressDTO shippingAddressDTO = new ShippingAddressDTO();
            shippingAddressDTO.setUser(user.getId());
            shippingAddressDTO.setReceiverName(receiver_name);
            shippingAddressDTO.setReceiverPhone(receiver_phone);
            shippingAddressDTO.setProvince(province);
            shippingAddressDTO.setDistrict(district);
            shippingAddressDTO.setWard(ward);
            shippingAddressDTO.setAddressDetail(address_detail);
            ShippingAddressEntity updateAddress = shippingAddressService.updateShippingAddress(user.getId(), id, shippingAddressDTO);
            ShippingAddressResponse response = ShippingAddressResponse.fromEntity(updateAddress);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Thêm địa chỉ thành công",
                    "data", response
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed",
                    "message", e.getMessage()
            ));
        }
    }
    @DeleteMapping("/delete-shipping-address")
    public ResponseEntity<?> deleteShippingAddress(@RequestParam("id") Long id){

        try {
            // Lấy thông tin người dùng từ token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Lấy user và cart tương ứng
            UserEntity user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));

            shippingAddressService.deleteShippingAddress(user.getId(), id);
//            ShippingAddressResponse response = ShippingAddressResponse.

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Xóa địa chỉ thành công"
            ));


        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed",
                    "message", e.getMessage()
            ));
        }
    }

    //lay ra doan thu theo ngay
    @GetMapping("/total-revenue-by-day")
    public ResponseEntity<?> totalRevenueByDay(
            @RequestParam("date")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        double totalRevenue = orderService.totalRevenueByDay(date);
        return ResponseEntity.ok(totalRevenue);
    }
    //lay ra doanh thu giua 2 ngay
    @GetMapping("/total-revenue-by-date-between")
    public ResponseEntity<?> totalRevenueByDateBetween(
            @RequestParam("start-date")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam("end-date")
    @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        double totalRevenue = orderService.totalRevenueByDateBetween(startDate, endDate);
        return ResponseEntity.ok(totalRevenue);
    }
    //lay ra doanh thu theo thang
    @GetMapping("/total-revenue-by-month")
    public ResponseEntity<?> totalRevenueByMonth(
            @RequestParam("month") int month,
            @RequestParam("year") int year) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        Date startDate = java.sql.Date.valueOf(start);
        Date endDate = java.sql.Date.valueOf(end);

        double totalRevenue = orderService.totalRevenueByMonth(startDate, endDate);
        return ResponseEntity.ok(totalRevenue);
    }
    //lay doanh thu theo nam
    @GetMapping("/total-revenue-by-year")
    public ResponseEntity<?> totalRevenueByYear(@RequestParam("year") int year) {
        double totalRevenue = orderService.totalRevenueByYear(year);
        return ResponseEntity.ok(totalRevenue);
    }

    @GetMapping("/list-total-revenue-by-year")
    public ResponseEntity<?> listTotalRevenueByYear(@RequestParam("year") int year) {
        List<Double> totalRevenue = orderService.listTotalRevenueByYear(year);
        return ResponseEntity.ok(totalRevenue);
    }

    @GetMapping("/list-total-revenue-by-month-in-year")
    public ResponseEntity<?> totalRevenueByMonthInYear(@RequestParam("year") int year) {
        Map<Integer, Double> revenueByMonth = orderService.totalRevenueByMonthInYear(year);
        return ResponseEntity.ok(revenueByMonth);
    }

    @GetMapping("/daily-revenue-between")
    public ResponseEntity<?> tongDoanhThuTheoKhoangNgay(
            @RequestParam("start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("end-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        Map<Date, Double> dailyRevenueBetween = orderService.totalRevenueByDateRange(startDate, endDate);
        return ResponseEntity.ok(dailyRevenueBetween);
    }
    @GetMapping("/top-10-best-selling-products")
    public ResponseEntity<List<BestSellingProductDTO>> getTop10BestSellingProducts() {
        List<BestSellingProductDTO> topProducts = orderService.getTop10BestSellingProducts();
        return ResponseEntity.ok(topProducts);
    }





    @GetMapping("/getStatus")
    public ResponseEntity<List<StatusOrderDTO>> getStatus() {
        try {
        List<StatusOrderEntity> statusOrderEntities = statusOrderRepository.findAll();

        List<StatusOrderDTO> statusOrderDTOS = statusOrderEntities.stream()
                .map(entity ->{
                    StatusOrderDTO statusOrderDTO = new StatusOrderDTO();
                    statusOrderDTO.setId(entity.getId());
                    statusOrderDTO.setName(entity.getName());
                    return statusOrderDTO;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(statusOrderDTOS);
    } catch (Exception e){
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    }




}
