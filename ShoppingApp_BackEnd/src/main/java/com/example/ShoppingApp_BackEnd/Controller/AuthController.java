package com.example.ShoppingApp_BackEnd.Controller;

import com.example.ShoppingApp_BackEnd.Data.User;
import com.example.ShoppingApp_BackEnd.Exception.AuthenticationException;
import com.example.ShoppingApp_BackEnd.Service.UserService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            if (loginRequest.getUsernameOrEmail() == null || loginRequest.getPassword() == null) {
                LOGGER.warn("Received login request with null username/email or password. Request: usernameOrEmail={}, password={}",
                        loginRequest.getUsernameOrEmail(), loginRequest.getPassword());

                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "Username/Email or password cannot be null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            User authenticatedUser = userService.authenticateUser(loginRequest.getUsernameOrEmail(), loginRequest.getPassword(), passwordEncoder);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("user", authenticatedUser);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            logAuthenticationFailure(loginRequest.getUsernameOrEmail(), loginRequest.getPassword());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid username/email or password");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }


    private Map<String, Object> createErrorResponse(String errorMessage) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("error", Map.of("message", errorMessage));
        return errorResponse;
    }

    private void logAuthenticationFailure(String usernameOrEmail, String password) {
        LOGGER.warn("Authentication failed for username/email: {} with password: {}", usernameOrEmail, password);
    }

    public static class LoginRequest {
        @JsonProperty("usernameOrEmail")
        private String usernameOrEmail;
        @JsonProperty("password")
        private String password;

        public String getUsernameOrEmail() {
            return usernameOrEmail;
        }

        public void setUsernameOrEmail(String usernameOrEmail) {
            this.usernameOrEmail = usernameOrEmail;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
