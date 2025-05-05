//package com.example.cdwebbackend.config;
//
//import jakarta.annotation.PostConstruct;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.io.UnsupportedEncodingException;
//import java.net.InetAddress;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.rmi.UnknownHostException;
//import java.util.*;
//
//import static com.mysql.cj.conf.PropertyKey.logger;
//
//
//@Component
//@RequiredArgsConstructor
//@Getter
//@Setter
//public class VNPayConfig {
//    private static final Logger logger = LoggerFactory.getLogger(VNPayConfig.class);
//    public static String vnp_Version = "2.1.0";
//    public static String vnp_Command = "pay";
//    @Value("${vnpay.pay-url}")
//    public  String vnp_PayUrl;
//
//    @Value("${vnpay.return-url}")
//    public  String vnp_Returnurl;
//
//    @Value("${vnpay.tmn-code}")
//    public  String vnp_TmnCode;
//
//    @Value("${vnpay.secret-key}")
//    public  String vnp_HashSecret;
//
//    @Value("${vnpay.api-url}")
//    public  String vnp_ApiUrl;
//
//    @PostConstruct
//    public void validateConfig() {
//        if (vnp_PayUrl == null || vnp_PayUrl.trim().isEmpty()) {
//            throw new IllegalStateException("VNPay pay-url is not configured");
//        }
//        if (vnp_Returnurl == null || vnp_Returnurl.trim().isEmpty()) {
//            throw new IllegalStateException("VNPay return-url is not configured");
//        }
//        if (vnp_TmnCode == null || vnp_TmnCode.trim().isEmpty()) {
//            throw new IllegalStateException("VNPay tmn-code is not configured");
//        }
//        if (vnp_HashSecret == null || vnp_HashSecret.trim().isEmpty()) {
//            throw new IllegalStateException("VNPay secret-key is not configured");
//        }
//        if (vnp_ApiUrl == null || vnp_ApiUrl.trim().isEmpty()) {
//            throw new IllegalStateException("VNPay api-url is not configured");
//        }
//    }
//
//    public static String getIpAddress(HttpServletRequest request) {
//        String ipAddress = request.getHeader("X-FORWARDED-FOR");
//        if (ipAddress == null || ipAddress.isEmpty()) {
//            ipAddress = request.getRemoteAddr();
//            if ("0:0:0:0:0:0:0:1".equals(ipAddress)) {
//                try {
//                    ipAddress = InetAddress.getLocalHost().getHostAddress();
//                } catch (java.net.UnknownHostException e) {
//                    logger.warn("Cannot resolve localhost address, defaulting to 127.0.0.1", e);
//                    ipAddress = "127.0.0.1";
//                }
//            }
//        }
//        logger.debug("Resolved IP address: {}", ipAddress);
//        return ipAddress;
//    }
//
//    public static String getRandomNumber(int len) {
//        Random rnd = new Random();
//        StringBuilder sb = new StringBuilder(len);
//        for (int i = 0; i < len; i++) {
//            sb.append((char) ('0' + rnd.nextInt(10)));
//        }
//        return sb.toString();
//    }
//
//    public static String hmacSHA512(String key, String data) {
//        try {
//            if (key == null || data == null) {
//                logger.error("Key or data is null for HMAC SHA512");
//                throw new IllegalArgumentException("Key or data cannot be null");
//            }
//            Mac hmac512 = Mac.getInstance("HmacSHA512");
//            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
//            hmac512.init(secretKey);
//            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
//            StringBuilder hash = new StringBuilder(2 * bytes.length);
//            for (byte b : bytes) {
//                String hex = Integer.toHexString(0xff & b);
//                if (hex.length() == 1) hash.append('0');
//                hash.append(hex);
//            }
//            logger.debug("Generated HMAC SHA512 hash: {}", hash.toString());
//            return hash.toString();
//        } catch (Exception ex) {
//            logger.error("Error generating HMAC SHA512 hash", ex);
//            throw new RuntimeException("Failed to generate HMAC SHA512 hash", ex);
//        }
//    }
//    public static String hashAllFields(Map<String, String> fields, String hashSecret) {
//        if (fields == null) {
//            logger.error("Fields map is null");
//            throw new IllegalArgumentException("Fields map cannot be null");
//        }
//        if (hashSecret == null || hashSecret.trim().isEmpty()) {
//            logger.error("HashSecret is null or empty");
//            throw new IllegalArgumentException("HashSecret cannot be null or empty");
//        }
//
//        try {
//            List<String> fieldNames = new ArrayList<>(fields.keySet());
//            Collections.sort(fieldNames);
//
//            StringBuilder hashData = new StringBuilder();
//            for (int i = 0; i < fieldNames.size(); i++) {
//                String fieldName = fieldNames.get(i);
//                String fieldValue = fields.get(fieldName);
//                if (fieldValue != null && !fieldValue.isEmpty()) {
//                    String encodedValue = URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString());
//                    hashData.append(fieldName).append('=').append(encodedValue);
//                    if (i < fieldNames.size() - 1) {
//                        hashData.append('&');
//                    }
//                }
//            }
//
//            if (hashData.length() == 0) {
//                logger.error("Hash data is empty, input fields: {}", fields);
//                throw new IllegalStateException("No valid fields to hash");
//            }
//
//            logger.debug("Hash data: {}", hashData.toString());
//            String secureHash = hmacSHA512(hashSecret, hashData.toString());
//            logger.debug("Generated secure hash: {}", secureHash);
//            return secureHash;
//        } catch (UnsupportedEncodingException ex) {
//            logger.error("Error encoding fields for hashing: {}", fields, ex);
//            throw new RuntimeException("Failed to encode fields for hashing", ex);
//        }
//    }
//}

        package com.example.cdwebbackend.config;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@RequiredArgsConstructor
@Getter
@Setter
public class VNPayConfig {
    private static final Logger logger = LoggerFactory.getLogger(VNPayConfig.class);
    public static String vnp_Version = "2.1.0";
    public static String vnp_Command = "pay";
    @Value("${vnpay.pay-url}")
    public String vnp_PayUrl;

    @Value("${vnpay.return-url}")
    public String vnp_Returnurl;

    @Value("${vnpay.tmn-code}")
    public String vnp_TmnCode;

    @Value("${vnpay.secret-key}")
    public String vnp_HashSecret;

    @Value("${vnpay.api-url}")
    public String vnp_ApiUrl;

    @PostConstruct
    public void validateConfig() {
        if (vnp_PayUrl == null || vnp_PayUrl.trim().isEmpty()) {
            throw new IllegalStateException("VNPay pay-url is not configured");
        }
        if (vnp_Returnurl == null || vnp_Returnurl.trim().isEmpty()) {
            throw new IllegalStateException("VNPay return-url is not configured");
        }
        if (vnp_TmnCode == null || vnp_TmnCode.trim().isEmpty()) {
            throw new IllegalStateException("VNPay tmn-code is not configured");
        }
        if (vnp_HashSecret == null || vnp_HashSecret.trim().isEmpty()) {
            throw new IllegalStateException("VNPay secret-key is not configured");
        }
        if (vnp_ApiUrl == null || vnp_ApiUrl.trim().isEmpty()) {
            throw new IllegalStateException("VNPay api-url is not configured");
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
            if ("0:0:0:0:0:0:0:1".equals(ipAddress)) {
                try {
                    ipAddress = InetAddress.getLocalHost().getHostAddress();
                } catch (java.net.UnknownHostException e) {
                    logger.warn("Cannot resolve localhost address, defaulting to 127.0.0.1", e);
                    ipAddress = "127.0.0.1";
                }
            }
        }
        logger.debug("Resolved IP address: {}", ipAddress);
        return ipAddress;
    }

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append((char) ('0' + rnd.nextInt(10)));
        }
        return sb.toString();
    }

    public static String hmacSHA512(String key, String data) {
        try {
            if (key == null || data == null) {
                logger.error("Key or data is null for HMAC SHA512");
                throw new IllegalArgumentException("Key or data cannot be null");
            }
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder(2 * bytes.length);
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hash.append('0');
                hash.append(hex);
            }
            logger.debug("Generated HMAC SHA512 hash: {}", hash.toString());
            return hash.toString();
        } catch (Exception ex) {
            logger.error("Error generating HMAC SHA512 hash", ex);
            throw new RuntimeException("Failed to generate HMAC SHA512 hash", ex);
        }
    }

    public static String hashAllFields(Map<String, String> fields, String hashSecret) {
        if (fields == null) {
            logger.error("Fields map is null");
            throw new IllegalArgumentException("Fields map cannot be null");
        }
        if (hashSecret == null || hashSecret.trim().isEmpty()) {
            logger.error("HashSecret is null or empty");
            throw new IllegalArgumentException("HashSecret cannot be null or empty");
        }

        try {
            List<String> fieldNames = new ArrayList<>(fields.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            for (int i = 0; i < fieldNames.size(); i++) {
                String fieldName = fieldNames.get(i);
                String fieldValue = fields.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    String encodedValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString());
                    hashData.append(fieldName).append('=').append(encodedValue);
                    if (i < fieldNames.size() - 1) {
                        hashData.append('&');
                    }
                }
            }

            if (hashData.length() == 0) {
                logger.error("Hash data is empty, input fields: {}", fields);
                throw new IllegalStateException("No valid fields to hash");
            }

            logger.debug("Hash data: {}", hashData.toString());
            String secureHash = hmacSHA512(hashSecret, hashData.toString());
            logger.debug("Generated secure hash: {}", secureHash);
            return secureHash;
        } catch (UnsupportedEncodingException ex) {
            logger.error("Error encoding fields for hashing: {}", fields, ex);
            throw new RuntimeException("Failed to encode fields for hashing", ex);
        }
    }
}
