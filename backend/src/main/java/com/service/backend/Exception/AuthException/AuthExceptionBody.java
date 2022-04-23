package com.service.backend.Exception.AuthException;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class AuthExceptionBody implements AuthExceptionInterface {

    private String message;

    private int statusCode;

    private String path;

    private String localDateTime = setDate();

    @Override
    public String setDate() {
        return String.valueOf(LocalDateTime.now());
    }

    @Override
    public void setAuthException(String message, int statusCode, String path) {
        this.message = message;
        this.statusCode = statusCode;
        this.path = path;
    }

    @Override
    public void reloadBean() {
        this.message = null;
        this.statusCode = 0;
        this.path = null;
    }
}
