package com.demo.firstProject.Controller.Account;

import com.demo.firstProject.Exception.BaseException;
import com.demo.firstProject.JPA.Entity.Account.AccountEntity;
import com.demo.firstProject.Service.Resource.Account.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AuthService authService;


    @PostMapping(path = "/accounts/login")
    public ResponseEntity accountControl_Login(HttpServletRequest request){

        HashMap<String, String> token = authService.login(request);

        return ResponseEntity.ok().body(token);
    }

    @GetMapping(path = "/accounts/refresh-token")
    public ResponseEntity accessDenied(HttpServletRequest request){
        // get refresh token
        String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        // check token not null
        if (refreshToken == null) {
            throw new BaseException("api.accounts.refreshToken -> refresh token is null.", HttpStatus.BAD_REQUEST);
        }

        System.out.println(refreshToken);

        // check token start with Bearer
        if (!refreshToken.startsWith("Bearer ")) {
            throw new BaseException("api.accounts.refreshToken -> Type of token not correct.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // remove Bearer
        String convertToken = refreshToken.substring(7);

        // use service
        HashMap<String, String> token = authService.refreshToken(convertToken);

        return ResponseEntity.ok().body(token);
    }

}
