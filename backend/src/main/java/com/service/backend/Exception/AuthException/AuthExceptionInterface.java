package com.service.backend.Exception.AuthException;

public interface AuthExceptionInterface {

    String setDate();

    void setAuthException(String message, int statusCode, String path);

    void reloadBean();

}
