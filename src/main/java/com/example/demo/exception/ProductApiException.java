package com.example.demo.exception;

public class ProductApiException extends RuntimeException {
    public ProductApiException(String message) {
        super(message);
    }

    public ProductApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
