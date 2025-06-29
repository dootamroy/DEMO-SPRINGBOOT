package com.example.demo1.controller;

import com.example.demo1.entity.User;
import com.example.demo1.service.UserService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size) {
        
        logger.info("Fetching users - page: {}, size: {}", page, size);
        
        Page<User> userPage = userService.getAllUsers(page, size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("timestamp", LocalDateTime.now());
        response.put("data", userPage.getContent());
        response.put("pagination", createPaginationInfo(userPage));
        response.put("message", "Users retrieved successfully");
        
        logger.info("Returning {} users for page {}", userPage.getContent().size(), page);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        logger.info("Fetching user with ID: {}", id);
        
        try {
            User user = userService.getUserById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("timestamp", LocalDateTime.now());
            response.put("data", user);
            response.put("message", "User retrieved successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching user with ID {}: {}", id, e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("error", e.getMessage());
            errorResponse.put("message", "Failed to retrieve user");
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user) {
        logger.info("Creating new user: {}", user.getName());
        
        try {
            User createdUser = userService.createUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("timestamp", LocalDateTime.now());
            response.put("data", createdUser);
            response.put("message", "User created successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("error", e.getMessage());
            errorResponse.put("message", "Failed to create user");
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody User user) {
        logger.info("Updating user with ID: {}", id);
        
        try {
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("timestamp", LocalDateTime.now());
            response.put("data", updatedUser);
            response.put("message", "User updated successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating user with ID {}: {}", id, e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("error", e.getMessage());
            errorResponse.put("message", "Failed to update user");
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);
        
        try {
            userService.deleteUser(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("timestamp", LocalDateTime.now());
            response.put("message", "User deleted successfully");
            response.put("deletedId", id);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error deleting user with ID {}: {}", id, e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("error", e.getMessage());
            errorResponse.put("message", "Failed to delete user");
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping(value = "/json-tree", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ObjectNode> getUsersAsJsonTree(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size) {
        
        logger.info("Fetching users as JSON tree - page: {}, size: {}", page, size);
        
        Page<User> userPage = userService.getAllUsers(page, size);
        
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("success", true);
        rootNode.put("timestamp", LocalDateTime.now().toString());
        rootNode.put("message", "Users retrieved successfully");
        
        ObjectNode paginationNode = rootNode.putObject("pagination");
        paginationNode.put("currentPage", userPage.getNumber());
        paginationNode.put("totalItems", userPage.getTotalElements());
        paginationNode.put("totalPages", userPage.getTotalPages());
        paginationNode.put("hasNext", userPage.hasNext());
        paginationNode.put("hasPrevious", userPage.hasPrevious());
        paginationNode.put("pageSize", userPage.getSize());
        
        rootNode.set("data", objectMapper.valueToTree(userPage.getContent()));
        
        return ResponseEntity.ok(rootNode);
    }

    private Map<String, Object> createPaginationInfo(Page<User> userPage) {
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("currentPage", userPage.getNumber());
        pagination.put("totalItems", userPage.getTotalElements());
        pagination.put("totalPages", userPage.getTotalPages());
        pagination.put("hasNext", userPage.hasNext());
        pagination.put("hasPrevious", userPage.hasPrevious());
        pagination.put("pageSize", userPage.getSize());
        return pagination;
    }
} 