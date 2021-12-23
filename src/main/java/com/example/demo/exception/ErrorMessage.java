package com.example.demo.exception;

import java.util.Date;

public record ErrorMessage(int statusCode, Date timestamp, String message) {

    public static ErrorMessage ofNow(int statusCode, String message) {
        return new ErrorMessage(statusCode, new Date(), message);
    }
}
