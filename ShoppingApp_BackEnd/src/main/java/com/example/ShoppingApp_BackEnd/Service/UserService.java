package com.example.ShoppingApp_BackEnd.Service;

import com.example.ShoppingApp_BackEnd.Data.User;
import com.example.ShoppingApp_BackEnd.Exception.AuthenticationException;
import com.example.ShoppingApp_BackEnd.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Long getUserCount() {
        return userRepository.count();
    }
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public User updateUser(Long userId, User updatedUser) {
        if (userRepository.existsById(userId)) {
            updatedUser.setId(userId);
            return userRepository.save(updatedUser);
        }
        return null; // Handle not found scenario
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public User authenticateUser(String usernameOrEmail, String password, PasswordEncoder passwordEncoder) {
        LOGGER.info("Received request with usernameOrEmail: {} and password: {}", usernameOrEmail, password);

        if (usernameOrEmail == null || usernameOrEmail.isEmpty() || password == null || password.isEmpty()) {
            LOGGER.error("Invalid input: Username/Email and password cannot be null or empty");
            throw new IllegalArgumentException("Username/Email and password cannot be null or empty");
        }

        Optional<User> user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user.get();
        } else {
            LOGGER.warn("Authentication failed for username/email: {} with password: {}", usernameOrEmail, password);
            throw new AuthenticationException("Invalid username/email or password for: " + usernameOrEmail);
        }
    }






}
