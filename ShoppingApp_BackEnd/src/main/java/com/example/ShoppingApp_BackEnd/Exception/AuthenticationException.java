package com.example.ShoppingApp_BackEnd.Exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }
}
