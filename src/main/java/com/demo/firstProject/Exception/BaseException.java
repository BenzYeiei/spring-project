package com.demo.firstProject.Exception;

import lombok.Data;
import org.springframework.http.HttpStatus;


@Data
public class BaseException extends RuntimeException {
    private HttpStatus Code;

    private String path = null;


    public BaseException(String message, HttpStatus code) {
        super(message);
        Code = code;
    }

    public BaseException(String message, HttpStatus code, String path) {
        super(message);
        this.Code = code;
        this.path = path;
    }
}
