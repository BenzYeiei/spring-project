package com.demo.firstProject.Exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ObjectException {

    private final String message;

    private final int httpStatusCode;

    private final Object payload;

    private final LocalDateTime timestamp;


    public ObjectException(String message, int httpStatusCode, Object payload, LocalDateTime timestamp) {
        this.message = message;
        this.httpStatusCode = httpStatusCode;
        this.payload = payload;
        this.timestamp = timestamp;
    }
}
