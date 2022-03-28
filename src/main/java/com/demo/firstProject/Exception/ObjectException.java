package com.demo.firstProject.Exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ObjectException {

    private final String message;

    private final int httpStatusCode;

    private final String path;

    private final LocalDateTime timestamp;


    public ObjectException(String message, int httpStatusCode, String path, LocalDateTime timestamp) {
        this.message = message;
        this.httpStatusCode = httpStatusCode;
        this.path = path;
        this.timestamp = timestamp;
    }
}
