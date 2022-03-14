package com.demo.firstProject.Exception.AuthException;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Date;

public interface AuthExceptionInterface {

    String setDate();

    void setAuthException(String message, int statusCode, String path);

    void reloadBean();

}
