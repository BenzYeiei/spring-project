package com.service.backend.Exception.AuthException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDenied implements AccessDeniedHandler {

    private AuthExceptionBody authExceptionBody;

    public CustomAccessDenied(AuthExceptionBody authExceptionBody) {
        this.authExceptionBody = authExceptionBody;
    }


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // set body response
        authExceptionBody.setAuthException(
                "The Role you have, not Access.",
                HttpStatus.FORBIDDEN.value(),
                request.getServletPath()
        );

        // set http response status code
        response.setStatus(HttpStatus.FORBIDDEN.value());

        // set Header as ContentType : application/json
        response.setContentType("application/json");

        // map authExceptionBody to json pass response.getOutputStream()
        new ObjectMapper().writeValue(response.getOutputStream(), authExceptionBody);

        // reset authExceptionBody because it is bean
        authExceptionBody.reloadBean();
    }
}
