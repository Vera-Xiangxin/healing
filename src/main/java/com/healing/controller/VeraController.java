package com.healing.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RestController
@RequestMapping("/api/vera")
@CrossOrigin(origins = "*")
public class VeraController {

    private static final String API_KEY = System.getenv("DOUBAO_API_KEY");
    private static final String API_URL = "https://api.zukijourney.com/v1/chat/completions";

    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody Map<String, Object> request) {
        String userMessage = (String) request.get("message");
        List<Map<String, String>> history = (List<Map<String, String>>) request.get("history");


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "doubao-lite-4k");
        requestBody.put("messages", history);
        requestBody.put("temperature", 0.75);

        // 添加用户最新消息
        ((List<Map<String, String>>) history).add(Map.of("role", "user", "content", userMessage));
        requestBody.put("messages", history);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, entity, Map.class);

        // 返回 AI 回复
        Map<String, Object> result = new HashMap<>();
        if (response.getStatusCode() == HttpStatus.OK) {
            Map body = response.getBody();
            List<Map> choices = (List<Map>) body.get("choices");
            String aiMessage = (String) ((Map) choices.get(0).get("message")).get("content");
            result.put("success", true);
            result.put("reply", aiMessage);
        } else {
            result.put("success", false);
            result.put("reply", "AI 服务暂时不可用");
        }
        return result;
    }
}