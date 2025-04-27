package com.example.cdwebbackend.controller;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/chat")
public class ChatBotController {

//    @Value("${openai.api.key}")
//    private String openaiApiKey;

    private static final
    String API_URL = "http://localhost:11434/api/chat";
    //String API_URL = "https://api.openai.com/v1/chat/completions";



//    @PostMapping("/send")
//    public ResponseEntity<String> sendMessage(
//            @RequestBody Map<String, String> body,
//            @RequestHeader("Authorization") String authorizationHeader) {
//
//        try {
//            String extractedToken = authorizationHeader.substring(7); // Bỏ "Bearer "
//            // (nếu cần kiểm tra token thì thêm ở đây)
//
//            String userMessage = body.get("userMessage");
//            if (userMessage == null || userMessage.trim().isEmpty()) {
//                return ResponseEntity.badRequest().body("Message is empty!");
//            }
//
//            // Escape JSON cho an toàn (nếu cần gửi thẳng text vào JSON)
//            userMessage = userMessage.replace("\"", "\\\"");
//
//            // Tạo JSON request gửi OpenAI
//            String requestBody = "{\n" +
//                    "  \"model\": \"gpt-3.5-turbo\",\n" +
//                    "  \"messages\": [{\"role\": \"user\", \"content\": \"" + userMessage + "\"}]\n" +
//                    "}";
//
//            // Gửi yêu cầu đến OpenAI API
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "Bearer " + openaiApiKey);
//            headers.setContentType(MediaType.APPLICATION_JSON); // <-- thêm dòng này
//
//            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
//
//            String response = restTemplate.postForObject(API_URL, entity, String.class);
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("Error: " + e.getMessage());
//        }
//    }
@PostMapping("/send")
public ResponseEntity<String> sendMessage(
        //ollama run llama3
        @RequestBody Map<String, String> body,
        @RequestHeader("Authorization") String authorizationHeader) {

    try {
        // (Có thể kiểm tra Authorization nếu bạn muốn. Ở đây mình bỏ qua luôn.)

        String userMessage = body.get("userMessage");

        if (userMessage == null || userMessage.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Message is empty!");
        }

        // Tạo JSON request gửi Ollama
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "llama3"); // Đúng model name
        payload.put("prompt", userMessage);
        payload.put("stream", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Chỉ cần Content-Type: application/json

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        RestTemplate restTemplate = new RestTemplate();

        String ollamaUrl = "http://localhost:11434/api/generate"; // Ollama API

        ResponseEntity<String> response = restTemplate.postForEntity(ollamaUrl, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            JSONObject json = new JSONObject(responseBody);

            String aiResponse = json.getString("response");

            return ResponseEntity.ok(aiResponse);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ollama server error");
        }


    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error: " + e.getMessage());
    }
}

}
