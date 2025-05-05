//package com.example.cdwebbackend.service.impl;
//
//import com.example.cdwebbackend.dto.OnlinePaymentDTO;
//import com.example.cdwebbackend.dto.PaymentDTO;
//import com.example.cdwebbackend.service.IVNPayService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@Service
//@RequiredArgsConstructor
//public class VNPayService implements IVNPayService {
//
//    private final VNPayConfig vnPayConfig;
//    private final VNPayUtils vnPayUtils;
//    @Override
//    public String createPaymentUrl(OnlinePaymentDTO paymentDTO, HttpServletRequest httpRequest){
//        String version = "2.1.0";
//        String command = "pay";
//        String orderType = "other";
//        long amount = paymentDTO.getTotalAmount();
//        String bankCode =paymentDTO.getBankCode();
//        String transactionReference = vnPayUtils.getRandomNumber(8);
//        String clientIpAddress = vnPayUtils.getIpAddress(httpRequest);
//        String terminalCode  = vnPayConfig.getVnpTmnCode();
//
//        Map<String, String> params = new HashMap<>();
//        params.put("vnp_Version", version);
//        params.put("vnp_Command", command);
//        params.put("vnp_TmnCode", terminalCode);
//        params.put("vnp_Amount", String.valueOf(amount));
//        params.put("vnp_CurrCode", "VND");
//
//        if(bankCode!=null && !bankCode.isEmpty()){
//            params.put("vnp_Bankcode", bankCode);
//        }
//        params.put("vnp_TxnRef". transactionReference);
//        params.put("vnp_OrderInfo", "Thanh toan don hang: "+ transactionReference);
//        params.put("vnp_OrderType", orderType);
//        String locale = paymentDTO.getLanguage();
//        if(locale!=null&&!locale.isEmpty()){
//            params.put("vnp_Locale", locale);
//        }else {
//            params.put("vnp_Locale", "vn");
//        }
//        params.put("vnp_ReturnUrl", vnPayConfig.getVnpReturnUrl());
//        params.put("vnp_IpAddr", clientIpAddress);
//
//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//        String createdDate = dateFormat.format(calendar.getTime());
//        params.put("vnp_CreateDate", createdDate);
//
//        calendar.add(Calendar.MINUTE, 15);
//        String expirationDate = dateFormat.format(calendar.getTime());
//        params.put("vnp_ExpireDate", expirationDate);
//
//        List<String> sortedFieldNames = new ArrayList<>(params.keySet());
//        Collections.sort(sortedFieldNames);
//
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder queryData = new StringBuilder();
//
//        for (Iterator<String> iterator = sortedFieldNames.iterator(); iterator.hasNext();){
//            String fieldName = iterator.next();
//            String fieldValue = params.get(fieldName);
//            if(fieldValue!=null&&!fieldValue.isEmpty()){
//                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
//                queryData.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
//                        .append('=')
//                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
//                if(iterator.hasNext()){
//                    hashData.append('&');
//                    queryData.append('&');
//                }
//            }
//        }
//        String secureHash = vnPayUtils.hmacSHA521(vnPayConfig.getSecretKey(), hashData.toString());
//        queryData.append("&vnp_SecureHash=").append(secureHash);
//        return vnPayConfig.getVnpUrl()+"?"+ queryData;
//
//    }
//
//    @Override
//    public String queryTransaction(PaymentQueryDTO queryDTO, HttpServletRequest httpServletRequest)throws IOException{
//        //chuan bi tham so cho VNPay
//        String requestId = vnPayUtils.getRandomNumber(8);
//        String version = "2.1.0";
//
//    }
//}
