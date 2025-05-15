
package com.example.cdwebbackend.controller;

import com.example.cdwebbackend.config.VNPayConfig;
import com.example.cdwebbackend.payload.ResponseObject;
import com.example.cdwebbackend.service.impl.OnlinePaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/payments")
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final OnlinePaymentService onlinePaymentService;
    @Autowired
    private VNPayConfig vnPayConfig;

    private enum VNPayResponseCode {
        SUCCESS("00", "Confirm Success"),
        ORDER_NOT_FOUND("01", "Order not found"),
        ORDER_ALREADY_CONFIRMED("02", "Order already confirmed"),
        INVALID_AMOUNT("04", "Invalid Amount"),
        INVALID_CHECKSUM("97", "Invalid Checksum"),
        INVALID_PARAM("99", "Invalid Parameter");

        private final String code;
        private final String message;

        VNPayResponseCode(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    @PostMapping(value = "/create-payment", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ResponseObject> createPayment(@RequestParam Map<String, String> params, HttpServletRequest req) {
        logger.info("Creating payment with params: {}", params);

        // Validate required parameters
        String validationError = validateRequiredParams(params);
        if (validationError != null) {
            logger.warn("Validation failed: {}", validationError);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseObject(VNPayResponseCode.INVALID_PARAM.getCode(),
                            validationError, null));
        }

        try {
            // Fix txt_inv_addr1 if undefined or empty
            if ("undefined".equals(params.get("txt_inv_addr1")) || params.get("txt_inv_addr1").trim().isEmpty()) {
                params.put("txt_inv_addr1", "Không có thông tin chi tiết");
            }

            int amount = Integer.parseInt(params.get("amount")) * 100;
            String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
            String vnp_IpAddr = VNPayConfig.getIpAddress(req);

            Map<String, String> vnp_Params = buildVnpParams(params, amount, vnp_TxnRef, vnp_IpAddr);
            String paymentUrl = generatePaymentUrl(vnp_Params);
            logger.info("Generated payment URL: {}", paymentUrl);

            ResponseObject response = new ResponseObject("00", "Success", paymentUrl);
           System.out.println("Response object before serialization: {}"+response);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (NumberFormatException e) {
            logger.error("Invalid amount format: {}", params.get("amount"), e);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseObject(VNPayResponseCode.INVALID_PARAM.getCode(),
                            "Invalid amount format", null));
        } catch (Exception e) {
            logger.error("Error creating payment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseObject("99", "Internal server error", null));
        }
    }
@PostMapping(value = "/ipn", produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<ResponseObject> handleVnPayIpn(HttpServletRequest request) {
    logger.info("Handling VNPay IPN");
    try {
        Map<String, String> fields = extractRequestParams(request);
        String vnp_SecureHash = fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        String hashCheck = VNPayConfig.hashAllFields(fields, vnPayConfig.vnp_HashSecret);
        if (!hashCheck.equals(vnp_SecureHash)) {
            logger.warn("Invalid checksum for IPN: {}", fields);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseObject(VNPayResponseCode.INVALID_CHECKSUM.getCode(),
                            VNPayResponseCode.INVALID_CHECKSUM.getMessage(), null));
        }

        String txnRef = fields.get("vnp_TxnRef");
        String vnp_Amount = fields.get("vnp_Amount");
        String vnp_ResponseCode = fields.get("vnp_ResponseCode");

        // Validate order
        Optional<com.example.cdwebbackend.entity.OnlinePaymentEntity> paymentOpt = onlinePaymentService.getOnlinePaymentRepository().findByOrderId(Long.parseLong(txnRef));
        if (!paymentOpt.isPresent()) {
            logger.warn("Order not found for txnRef: {}", txnRef);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseObject(VNPayResponseCode.ORDER_NOT_FOUND.getCode(),
                            VNPayResponseCode.ORDER_NOT_FOUND.getMessage(), null));
        }

        com.example.cdwebbackend.entity.OnlinePaymentEntity payment = paymentOpt.get();
        com.example.cdwebbackend.entity.OrderEntity order = payment.getOrder();

        // Check vnp_Amount
        long receivedAmount;
        try {
            receivedAmount = Long.parseLong(vnp_Amount);
        } catch (NumberFormatException e) {
            logger.warn("Invalid vnp_Amount format: {}", vnp_Amount);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseObject(VNPayResponseCode.INVALID_AMOUNT.getCode(),
                            "Invalid vnp_Amount format", null));
        }

        long expectedAmount = order.getTotalPrice() * 100;
        if (expectedAmount != receivedAmount) {
            logger.warn("Invalid amount for txnRef: {}, expected: {}, received: {}",
                    txnRef, expectedAmount, receivedAmount);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseObject(VNPayResponseCode.INVALID_AMOUNT.getCode(),
                            VNPayResponseCode.INVALID_AMOUNT.getMessage(), null));
        }

        if (order.getStatusOrder().getId() == 11) {
            logger.warn("Order already confirmed for txnRef: {}", txnRef);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseObject(VNPayResponseCode.ORDER_ALREADY_CONFIRMED.getCode(),
                            VNPayResponseCode.ORDER_ALREADY_CONFIRMED.getMessage(), null));
        }

        // Update payment and order status
        int orderStatus = "00".equals(vnp_ResponseCode) ? 11 : 12;
        String paymentStatus = "00".equals(vnp_ResponseCode) ? "Đã thanh toán" : "Thất bại";
//        onlinePaymentService.updatePaymentAndOrderStatus(txnRef, paymentStatus, orderStatus);
        logger.info("Updated payment status to {} and order status to {} for txnRef: {}", paymentStatus, orderStatus, txnRef);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ResponseObject(VNPayResponseCode.SUCCESS.getCode(),
                        VNPayResponseCode.SUCCESS.getMessage(), null));
    } catch (Exception e) {
        logger.error("Error handling IPN", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ResponseObject("99", "Internal server error", null));
    }
}

    @GetMapping(value = "/return", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> vnpayReturn(HttpServletRequest request) {
        logger.info("Handling VNPay return");
        try {
            Map<String, String> fields = extractRequestParams(request);
            String vnp_SecureHash = fields.remove("vnp_SecureHash");
            fields.remove("vnp_SecureHashType");

            String signValue = VNPayConfig.hashAllFields(fields, vnPayConfig.vnp_HashSecret);
            if (!signValue.equals(vnp_SecureHash)) {
                logger.warn("Invalid signature for return: {}", fields);
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseObject(VNPayResponseCode.INVALID_CHECKSUM.getCode(),
                                "Invalid signature", null));
            }

            String txnRef = fields.get("vnp_TxnRef");
            String id_order = fields.get("vnp_OrderInfo");
            System.out.println("vnp_OrderInfo   "+ id_order);
            String vnp_ResponseCode = fields.get("vnp_ResponseCode");

            // Validate order
            Optional<com.example.cdwebbackend.entity.OnlinePaymentEntity> paymentOpt = onlinePaymentService.getOnlinePaymentRepository().findByOrderId(Long.parseLong(txnRef));
//            if (!paymentOpt.isPresent()) {
//                logger.warn("Order not found for txnRef: {}", txnRef);
//                System.out.println("Order not found for txnRef: {}   "+ txnRef);
//                return ResponseEntity.ok()
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(new ResponseObject(VNPayResponseCode.ORDER_NOT_FOUND.getCode(),
//                                VNPayResponseCode.ORDER_NOT_FOUND.getMessage(), null));
//            }

            // Update payment and order status
            int orderStatus = "00".equals(vnp_ResponseCode) ? 11 : 12;
            String paymentStatus = "00".equals(vnp_ResponseCode) ? "Đã thanh toán" : "Thất bại";
//            onlinePaymentService.updatePaymentAndOrderStatus(txnRef, paymentStatus, orderStatus);
            logger.info("Updated payment status to {} and order status to {} for txnRef: {}", paymentStatus, orderStatus, txnRef);

            if ("00".equals(vnp_ResponseCode)) {
                onlinePaymentService.updatePaymentStatus(id_order, 1L);

                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseObject("00", "Transaction successful", Map.of("id", id_order)));
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseObject("99", "Transaction failed", null));
            }
        } catch (Exception e) {
            logger.error("Error handling return", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseObject("99", "Internal server error", null));
        }
    }

    private String validateRequiredParams(Map<String, String> params) {
        String[] requiredParams = {
                "amount", "vnp_OrderInfo", "ordertype", "txt_billing_mobile",
                "txt_billing_email", "txt_billing_fullname", "txt_bill_city",
                "txt_bill_country", "txt_inv_mobile", "txt_inv_email", "txt_inv_customer"
        };

        for (String param : requiredParams) {
            if (!params.containsKey(param) || params.get(param) == null || params.get(param).trim().isEmpty()) {
                return "Missing or empty parameter: " + param;
            }
        }

        if (!params.containsKey("txt_inv_addr1")) {
            params.put("txt_inv_addr1", "Không có thông tin chi tiết");
        }

        return null;
    }

    private Map<String, String> buildVnpParams(Map<String, String> params, int amount, String vnp_TxnRef, String vnp_IpAddr) {
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (params.containsKey("bankcode") && !params.get("bankcode").isEmpty()) {
            //vnp_Params.put("vnp_BankCode", params.get("bankcode"));
            vnp_Params.put("vnp_BankCode", "VNPAYQR");
        }


        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", params.get("vnp_OrderInfo"));
        vnp_Params.put("vnp_OrderType", params.get("ordertype"));
        vnp_Params.put("vnp_Locale", params.getOrDefault("language", "vn"));
        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        try {
            vnp_Params.put("vnp_Bill_Mobile", URLEncoder.encode(params.get("txt_billing_mobile"), StandardCharsets.UTF_8.toString()));
            vnp_Params.put("vnp_Bill_Email", URLEncoder.encode(params.get("txt_billing_email"), StandardCharsets.UTF_8.toString()));
            String fullName = params.get("txt_billing_fullname");
            if (fullName != null && !fullName.trim().isEmpty()) {
                int idx = fullName.indexOf(' ');
                if (idx > 0) {
                    vnp_Params.put("vnp_Bill_FirstName", URLEncoder.encode(fullName.substring(0, idx), StandardCharsets.UTF_8.toString()));
                    vnp_Params.put("vnp_Bill_LastName", URLEncoder.encode(fullName.substring(idx + 1), StandardCharsets.UTF_8.toString()));
                } else {
                    vnp_Params.put("vnp_Bill_FirstName", URLEncoder.encode(fullName, StandardCharsets.UTF_8.toString()));
                    vnp_Params.put("vnp_Bill_LastName", "");
                }
            }
            vnp_Params.put("vnp_Bill_Address", URLEncoder.encode(params.getOrDefault("txt_inv_addr1", "Không có thông tin chi tiết"), StandardCharsets.UTF_8.toString()));
            vnp_Params.put("vnp_Bill_City", URLEncoder.encode(params.get("txt_bill_city"), StandardCharsets.UTF_8.toString()));
            vnp_Params.put("vnp_Bill_Country", URLEncoder.encode(params.get("txt_bill_country"), StandardCharsets.UTF_8.toString()));
            if (params.containsKey("txt_bill_state") && !params.get("txt_bill_state").isEmpty()) {
                vnp_Params.put("vnp_Bill_State", URLEncoder.encode(params.get("txt_bill_state"), StandardCharsets.UTF_8.toString()));
            }
            vnp_Params.put("vnp_Inv_Phone", URLEncoder.encode(params.get("txt_inv_mobile"), StandardCharsets.UTF_8.toString()));
            vnp_Params.put("vnp_Inv_Email", URLEncoder.encode(params.get("txt_inv_email"), StandardCharsets.UTF_8.toString()));
            vnp_Params.put("vnp_Inv_Customer", URLEncoder.encode(params.get("txt_inv_customer"), StandardCharsets.UTF_8.toString()));
            vnp_Params.put("vnp_Inv_Address", URLEncoder.encode(params.getOrDefault("txt_inv_addr1", "Không có thông tin chi tiết"), StandardCharsets.UTF_8.toString()));
            vnp_Params.put("vnp_Inv_Company", URLEncoder.encode(params.getOrDefault("txt_inv_company", "N/A"), StandardCharsets.UTF_8.toString()));
            vnp_Params.put("vnp_Inv_Taxcode", URLEncoder.encode(params.getOrDefault("txt_inv_taxcode", "N/A"), StandardCharsets.UTF_8.toString()));
            vnp_Params.put("vnp_Inv_Type", params.getOrDefault("cbo_inv_type", "I"));
        } catch (UnsupportedEncodingException e) {
            logger.error("Error encoding parameters: {}", e.getMessage());
        }

        logger.debug("vnp_Params before signing: {}", vnp_Params);
        return vnp_Params;
    }

    private String generatePaymentUrl(Map<String, String> vnp_Params) throws UnsupportedEncodingException {
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext(); ) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString())).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String vnp_SecureHash = VNPayConfig.hmacSHA512(vnPayConfig.vnp_HashSecret, hashData.toString());
        logger.debug("hashData: {}", hashData.toString());
        logger.debug("Generated vnp_SecureHash: {}", vnp_SecureHash);
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        return vnPayConfig.vnp_PayUrl + "?" + query;
    }

    private Map<String, String> extractRequestParams(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                fields.put(fieldName, fieldValue);
            }
        }
        return fields;
    }
}
