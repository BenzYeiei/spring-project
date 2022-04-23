package com.service.backend.Filter;

import com.service.backend.Exception.AuthException.AuthExceptionBody;
import com.service.backend.Component.JsonwebtokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CheckTokenFilter extends OncePerRequestFilter {

    private JsonwebtokenService jsonwebtokenService;

    private AuthExceptionBody authExceptionBody;

    public CheckTokenFilter(JsonwebtokenService jsonwebtokenService, AuthExceptionBody authExceptionBody) {
        this.jsonwebtokenService = jsonwebtokenService;
        this.authExceptionBody = authExceptionBody;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // get path equal "/api/accounts/refresh-token"
        boolean isPath = request.getServletPath().equals("/api/accounts/refresh-token");
        // get Authorization header
        String isAuthorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        // TODO: Public api
        if (isAuthorization == null) {
            filterChain.doFilter(request, response);
            return; // return for exit programming because after filterChain.doFilter() program execute next below line.
        }

        // TODO: refresh token
        if (isPath) {
            filterChain.doFilter(request, response);
            return; // return for exit programming because after filterChain.doFilter() program execute next below line.
        }

        // TODO: verify token
        // path not '/api/login' and Authorization not null
        if (isAuthorization != null) {
            if (!isAuthorization.startsWith("Bearer ")) {

                // set exception for error in condition
                authExceptionBody.setAuthException(
                        "Authorization type is not correct.",
                        HttpStatus.FORBIDDEN.value(),
                        request.getServletPath()
                );

                // chain filter to spring security check Authentication
                // spring will response 403 or execute AuthenticationEntryPoint
                filterChain.doFilter(request, response);
                return;
            }

            // get remove Bearer character
            String getToken = isAuthorization.substring(7);

            // get springToken
            UsernamePasswordAuthenticationToken springToken = jsonwebtokenService.verifyToken(getToken, request.getServletPath());

            if (springToken.getPrincipal() == null) {
                // chain filter to spring security check Authentication
                // spring will response 403 or execute AuthenticationEntryPoint
                filterChain.doFilter(request, response);

                // for use public api but send http authorization header
                authExceptionBody.reloadBean();
                return;
            }

            // send springToken to spring
            SecurityContextHolder.getContext().setAuthentication(springToken);

            filterChain.doFilter(request, response);
            return; // return for exit programming because after filterChain.doFilter() program execute next below line.
        }

    }
}
