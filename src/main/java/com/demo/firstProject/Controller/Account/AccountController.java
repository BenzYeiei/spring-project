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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AuthService authService;


    @GetMapping(path = "/email")
    public ResponseEntity accountControl_getAccount(
        @RequestParam(name = "username", required = false) String username,
        HttpServletRequest request
    ) {
        //
        if (username.isEmpty()) {
            throw new BaseException("email is null", HttpStatus.BAD_REQUEST, request.getServletPath());
        }

        return ResponseEntity.ok().body(null);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<HashMap<String, String>> accountControl_Login(HttpServletRequest request){

        HashMap<String, String> token = authService.login(request);

        return ResponseEntity.ok().body(token);
    }

    @GetMapping(path = "/refresh-token")
    public ResponseEntity<HashMap<String, String>> accessDenied(HttpServletRequest request){
        // get refresh token
        String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        // check token not null
        if (refreshToken == null) {
            throw new BaseException("api.accounts.refreshToken -> refresh token is null.", HttpStatus.BAD_REQUEST);
        }

        // check token start with Bearer
        if (!refreshToken.startsWith("Bearer ")) {
            throw new BaseException("api.accounts.refreshToken -> Type of token not correct.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // remove Bearer
        String convertToken = refreshToken.substring(7);

        // use service
        HashMap<String, String> token = authService.refreshToken(convertToken, request);

        return ResponseEntity.ok().body(token);
    }

}
