package com.demo.firstProject.Service.Resource.Account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.demo.firstProject.Exception.BaseException;
import com.demo.firstProject.JPA.Entity.Account.AccountEntity;
import com.demo.firstProject.JPA.Entity.Account.RoleEntity;
import com.demo.firstProject.JPA.Repository.Account.AccountRepository;
import com.demo.firstProject.JPA.Repository.Account.RoleRepository;
import com.demo.firstProject.Service.ServiceModel.RegisterService.AuthModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService implements AuthModel {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JsonwebtokenService jsonwebtokenService;


    @Override // TODO: LOGIN
    public HashMap login(HttpServletRequest request) {
        // get email and password
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // validate email and password
        if (email == null || password == null) {
            throw new BaseException("api.login -> email or password is null.", HttpStatus.BAD_REQUEST);
        }

        // get account entity
        AccountEntity accountEntityResult = accountRepository.findByEmail(email);

        // verify email
        if (accountEntityResult == null) {
            throw new BaseException("api.login -> email not correct.", HttpStatus.BAD_REQUEST);
        }

        // verify password
        boolean isPassword = passwordEncoder.matches(password, accountEntityResult.getPassword());
        if (!isPassword) {
            throw new BaseException("api.login -> password not correct.", HttpStatus.BAD_REQUEST);
        }

        // convert List RoleEntity to List String
        List<String> getAuthorities = accountEntityResult.getRoles().stream().map(
                result -> result.getRoleName()
        ).collect(Collectors.toList());

        // access token
        String accessToken = jsonwebtokenService.genAccessToken(
                accountEntityResult.getId(),
                getAuthorities
        );

        // refresh token
        String refreshToken = jsonwebtokenService.genRefreshToken(
                accountEntityResult.getId()
        );

        return new HashMap(){{
            put("accessToken", accessToken);
            put("refreshToken", refreshToken);
        }};
    }

    @Override
    public HashMap<String, String> refreshToken(String refreshToken) {
        // get id or error
        String getId_or_err = jsonwebtokenService.verityRefreshToken(refreshToken);

        // check error
        if (getId_or_err.startsWith("err")) {
            throw new BaseException(
                    "api.accounts.refreshToken -> " + getId_or_err.substring(3),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // get account data
        Optional<AccountEntity> accountEntityResult = accountRepository.findById(getId_or_err);

        // check account data is not null
        if (accountEntityResult.isEmpty()) {
            throw new BaseException("api.accounts.refreshToken -> refresh token not have user.", HttpStatus.BAD_REQUEST);
        }

        // account data from optional
        AccountEntity getAccount = accountEntityResult.get();

        // get list of role
        List<String> authorities = getAccount.getRoles().stream().map(RoleEntity::getRoleName).collect(Collectors.toList());

        // get access token
        String accessToken = jsonwebtokenService.genAccessToken(getAccount.getId(), authorities);

        return new HashMap(){{
            put("accessToken", accessToken);
            put("refreshToken", refreshToken);
        }};
    }

}
