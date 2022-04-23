package com.service.backend.Service.ServiceModel.AccountService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public interface Jsonwebtoken {
    String genAccessToken(String id, List<String> roles, String path);

    String genRefreshToken(String id, String path);

    UsernamePasswordAuthenticationToken verifyToken(String jwt, String path);

    String verityRefreshToken(String refreshToken, String path);
}
