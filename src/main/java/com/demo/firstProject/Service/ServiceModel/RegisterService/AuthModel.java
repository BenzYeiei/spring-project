package com.demo.firstProject.Service.ServiceModel.RegisterService;

import com.demo.firstProject.JPA.Entity.Account.AccountEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

public interface AuthModel {
    HashMap<String, String> login(HttpServletRequest request);

    HashMap<String, String> refreshToken(String refreshToken);

}
