package com.demo.firstProject.Service.ServiceModel.AccountService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public interface AuthModel {
    HashMap<String, String> login(HttpServletRequest request);

    HashMap<String, String> refreshToken(String refreshToken, HttpServletRequest request);

}
