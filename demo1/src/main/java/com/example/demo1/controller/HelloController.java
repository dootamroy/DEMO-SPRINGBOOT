package com.example.demo1.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private static final Logger logger = LogManager.getLogger(HelloController.class);

    @GetMapping("/hello")
    public String hello() {
        logger.info("Hello from demo1");
        return "Hello from demo1";
    }

    @GetMapping("/errors/{type}")
    public String generateError(@PathVariable String type) {
        try {
            switch (type.toLowerCase()) {
                case "arithmetic":
                    int result = 1 / 0; // This will cause ArithmeticException
                    return "This won't be reached";
                case "null":
                    String str = null;
                    return str.length() + ""; // This will cause NullPointerException
                case "array":
                    int[] arr = new int[1];
                    return arr[2] + ""; // This will cause ArrayIndexOutOfBoundsException
                default:
                    throw new IllegalArgumentException("Unknown error type: " + type);
            }
        } catch (Exception e) {
            logger.error("Error occurred in demo1: " + e.getMessage(), e);
            throw e; // Re-throw to let Spring handle it
        }
    }
} 