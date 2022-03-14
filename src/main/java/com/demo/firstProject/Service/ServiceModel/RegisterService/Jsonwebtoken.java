package com.demo.firstProject.Service.ServiceModel.RegisterService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public interface Jsonwebtoken {
    String genAccessToken(String id, List<String> roles);

    String genRefreshToken(String id);

    UsernamePasswordAuthenticationToken verifyToken(String jwt, String path);

    String verityRefreshToken(String refreshToken);
}
