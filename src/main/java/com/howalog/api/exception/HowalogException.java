package com.howalog.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class HowalogException extends RuntimeException {

    private Map<String, String> validation = new HashMap<>();

    public HowalogException(String message) {
        super(message);
    }

    public HowalogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
