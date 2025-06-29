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
    public long getTotalUsers() {
        return userRepository.count();
    }
} 