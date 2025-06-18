package com.example.demo2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@RestController
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
    private final RestTemplate restTemplate;

    @Autowired
    public HelloController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/hello")
    public String hello() {
        logger.info("Hello from demo2");
        
        try {
            // Using the service name instead of hardcoded URL
            String demo1ServiceUrl = "http://demo1-service/api/users?page=10&size=1000";
            logger.info("Fetching users from demo1 service: {}", demo1ServiceUrl);
            
            Map<String, Object> response = restTemplate.getForObject(demo1ServiceUrl, Map.class);
            
            if (response != null) {
                logger.info("Successfully fetched users from demo1. Total items: {}", 
                    response.get("totalItems"));
                return "Hello from demo2! Fetched users from demo1. Total items: " + 
                    response.get("users");
            } else {
                logger.warn("Received null response from demo1 service");
                return "Hello from demo2! Failed to fetch users from demo1.";
            }
        } catch (Exception e) {
            logger.error("Error fetching users from demo1: {}", e.getMessage(), e);
            return "Hello from demo2! Error fetching users from demo1: " + e.getMessage();
        }
    }
}