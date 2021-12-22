package com.example.demo.exception;

public class ParsingProductException extends ProductApiException {

    public ParsingProductException(String message) {
        super(message);
    }

    public ParsingProductException(String message, Throwable cause) {
        super(message, cause);
    }
}
