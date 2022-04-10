package com.demo.firstProject.Service.Resource.Account;

import com.demo.firstProject.Component.JsonwebtokenService;
import com.demo.firstProject.Exception.BaseException;
import com.demo.firstProject.JPA.Entity.Account.AccountEntity;
import com.demo.firstProject.JPA.Entity.Account.RoleEntity;
import com.demo.firstProject.JPA.Repository.Account.AccountRepository;
import com.demo.firstProject.JPA.Repository.Account.RoleRepository;
import com.demo.firstProject.Service.ServiceModel.AccountService.AuthModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
    public HashMap<String, String> login(HttpServletRequest request) {
        // get username and password
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // validate username and password
        if (username == null) {
            throw new BaseException("username is null.", HttpStatus.BAD_REQUEST, request.getServletPath());
        }

        // validate username and password
        if (password == null) {
            throw new BaseException("password is null.", HttpStatus.BAD_REQUEST, request.getServletPath());
        }

        // get account entity
        Optional<AccountEntity> accountEntityResult = accountRepository.findByUsername(username);

        // verify username
        if (accountEntityResult.isEmpty()) {
            throw new BaseException("username not correct.", HttpStatus.BAD_REQUEST, request.getServletPath());
        }

        // get account data
        AccountEntity account = accountEntityResult.get();

        // verify password
        boolean isPassword = passwordEncoder.matches(password, account.getPassword());
        if (!isPassword) {
            throw new BaseException("password not correct.", HttpStatus.BAD_REQUEST, request.getServletPath());
        }

        // convert List RoleEntity to List String
        List<String> getAuthorities = account.getRoles().stream().map(
                RoleEntity::getRoleName
        ).collect(Collectors.toList());

        // access token
        String accessToken = jsonwebtokenService.genAccessToken(
                account.getId(),
                getAuthorities,
                request.getServletPath()
        );

        // refresh token
        String refreshToken = jsonwebtokenService.genRefreshToken(
                account.getId(),
                request.getServletPath()
        );

        // set token for return
        HashMap<String, String> body = new HashMap<>();
        body.put("accessToken", accessToken);
        body.put("refreshToken", refreshToken);

        // create log success
        log.info("login with access token:{}, refresh token:{}", accessToken, refreshToken);

        return body;
    }

    @Override
    public HashMap<String, String> refreshToken(String refreshToken, HttpServletRequest request) {
        // get id or error
        String getId = jsonwebtokenService.verityRefreshToken(refreshToken, request.getServletPath());

        // get account data
        Optional<AccountEntity> accountEntityResult = accountRepository.findById(getId);

        // check account data is not null
        if (accountEntityResult.isEmpty()) {
            throw new BaseException("refresh token not have user.", HttpStatus.BAD_REQUEST, request.getServletPath());
        }

        // account data from optional
        AccountEntity getAccount = accountEntityResult.get();

        // get list of role
        List<String> authorities = getAccount.getRoles().stream().map(RoleEntity::getRoleName).collect(Collectors.toList());

        // gen access token
        String genAccessToken = jsonwebtokenService.genAccessToken(
                getAccount.getId(),
                authorities,
                request.getServletPath()
        );

        // gen refresh token
        String genRefreshToken = jsonwebtokenService.genRefreshToken(
                getAccount.getId(),
                request.getServletPath()
        );

        // set token for return
        HashMap<String, String> body = new HashMap<>();
        body.put("accessToken", genAccessToken);
        body.put("refreshToken", genRefreshToken);

        // create log success
        log.info("login with access token:{}, refresh token:{}", genAccessToken, genRefreshToken);

        return body;
    }

}
