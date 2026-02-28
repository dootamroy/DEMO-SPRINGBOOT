package com.example.demo1.service;

import com.example.demo1.entity.User;
import com.example.demo1.exception.ConflictException;
import com.example.demo1.exception.ResourceNotFoundException;
import com.example.demo1.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserByIdThrowsNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    void createUserThrowsConflictWhenEmailExists() {
        User user = new User("A", "a@x.com");
        when(userRepository.findByEmail("a@x.com")).thenReturn(Optional.of(new User("B", "a@x.com")));

        assertThrows(ConflictException.class, () -> userService.createUser(user));
    }
}
