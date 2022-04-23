package com.service.backend.Exception.AuthException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private AuthExceptionBody authExceptionBody;

    public CustomAuthenticationEntryPoint(AuthExceptionBody authExceptionBody) {
        this.authExceptionBody = authExceptionBody;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        // set Header as ContentType : application/json
        response.setContentType("application/json");

        if (authExceptionBody.getMessage() != null) {

            // set status to response
            response.setStatus(authExceptionBody.getStatusCode());

            // add authExceptionBody to body response
            new ObjectMapper().writeValue(response.getOutputStream(), authExceptionBody);
        } else {
            // create hash for body response
            HashMap<String, String> body = new HashMap<>(){{
                // initialization body of response
                put("statusCode", String.valueOf(HttpStatus.FORBIDDEN.value()));
                put("message", authException.getMessage());
                put("path", request.getServletPath());
                put("time", LocalDateTime.now().toString());
            }};

            // set status to response
            response.setStatus(403);

            // add body to body response
            new ObjectMapper().writeValue(response.getOutputStream(), body);
        }

        // reset property in bean
        authExceptionBody.reloadBean();

    }
}
