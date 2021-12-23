package com.example.demo.exception;

public class BatchHandleException extends ProductApiException {

    public BatchHandleException(String message) {
        super(message);
    }

    public BatchHandleException(String message, Throwable cause) {
        super(message, cause);
    }
}
