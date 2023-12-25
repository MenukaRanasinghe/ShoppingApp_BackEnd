package com.example.ShoppingApp_BackEnd.Controller;

import com.example.ShoppingApp_BackEnd.Data.User;
import com.example.ShoppingApp_BackEnd.Exception.AuthenticationException;
import com.example.ShoppingApp_BackEnd.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.hibernate.bytecode.enhance.spi.interceptor.BytecodeInterceptorLogging.LOGGER;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

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


    private void logAuthenticationFailure(String usernameOrEmail, String password) {
        LOGGER.warn("Authentication failed for username/email: {} with password: {}");
    }

    public static class LoginRequest {
        private String usernameOrEmail;
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
