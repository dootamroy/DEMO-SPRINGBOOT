package com.example.demo1.service;

import com.example.demo1.entity.User;
import com.example.demo1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final int DEFAULT_PAGE_SIZE = 1000;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true, transactionManager = "renderTransactionManager")
    public Page<User> getAllUsers(int page, int size) {
        // Ensure size is within reasonable limits
        int pageSize = Math.min(size, DEFAULT_PAGE_SIZE);
        
        // Create pageable with sorting by id
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id"));
        
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true, transactionManager = "renderTransactionManager")
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User not found with ID: " + id);
        }
    }

    @Transactional(transactionManager = "renderTransactionManager")
    public User createUser(User user) {
        // Set creation timestamp if not already set
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        
        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        
        return userRepository.save(user);
    }

    @Transactional(transactionManager = "renderTransactionManager")
    public User updateUser(User user) {
        // Check if user exists
        if (!userRepository.existsById(user.getId())) {
            throw new RuntimeException("User not found with ID: " + user.getId());
        }
        
        // Check if email is being changed and if it already exists
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent()) {
            User currentUser = existingUser.get();
            if (!currentUser.getEmail().equals(user.getEmail())) {
                // Email is being changed, check if new email already exists
                if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                    throw new RuntimeException("User with email " + user.getEmail() + " already exists");
                }
            }
        }
        
        return userRepository.save(user);
    }

    @Transactional(transactionManager = "renderTransactionManager")
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true, transactionManager = "renderTransactionManager")
    public long getTotalUsers() {
        return userRepository.count();
    }
} 