package com.example.demo.domain;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public enum MeasureUnit {
    KG("kg"),
    UNIT("unit");

    private final String value;
    
    public static MeasureUnit forString(String value) {
        for (MeasureUnit type : values()) {
            if (Objects.equals(type.value, value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid MeasureUnit code: " + value);
    }
}
