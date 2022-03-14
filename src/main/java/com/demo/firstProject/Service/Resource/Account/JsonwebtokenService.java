package com.demo.firstProject.Service.Resource.Account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.demo.firstProject.Exception.AuthException.AuthExceptionBody;
import com.demo.firstProject.Exception.BaseException;
import com.demo.firstProject.Service.ServiceModel.RegisterService.Jsonwebtoken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JsonwebtokenService implements Jsonwebtoken {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.issuer}")
    private String isSuer;

    @Autowired
    private AuthExceptionBody authExceptionBody;


    @Override
    public String genAccessToken(String id, List<String> roles) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            String token = JWT.create()
                    // uuid
                    .withSubject(id)
                    // authorities
                    .withClaim("authorities", roles)
                    // path of controller
                    .withIssuer(isSuer)
                    // date class init current time use millisecond and plus 1,800,000 millisecond from 30 * 60 * 1000
                    // 30 minute
//                    .withExpiresAt(new Date(System.currentTimeMillis() + (60 * 1000)))
                    .withExpiresAt(new Date(System.currentTimeMillis() + (30 * 60 * 1000)))
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
//            throw new BaseException(
//                    "api.auth -> generate access token error. because: " + exception.getMessage(),
//                    HttpStatus.INTERNAL_SERVER_ERROR
//            );
            return null;
        }
    }

    @Override
    public String genRefreshToken(String id) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            String token = JWT.create()
                    // uuid
                    .withSubject(id)
                    // path of controller
                    .withIssuer(isSuer)
                    // date class init current time use millisecond and dplus 108,000,000 millisecon from 30 * 60 * 60 * 1000
                    // 30 hour
                    .withExpiresAt(new Date(System.currentTimeMillis() + (30 * 60 * 60 * 1000)))
                    .sign(algorithm);
            return token;

        } catch (JWTCreationException exception) {
//            throw new BaseException(
//                    "api.auth -> generate access token error. because: " + exception.getMessage(),
//                    HttpStatus.INTERNAL_SERVER_ERROR
//            );
            return null;
        }
    }

    @Override
    public UsernamePasswordAuthenticationToken verifyToken(String jsonWebToken, String path) {
        try {
            // get algorithm
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            // set verifier method for verify
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(isSuer)
                    .build(); //Reusable verifier instance

            // decoded
            DecodedJWT verifyResult = verifier.verify(jsonWebToken);

            String getId = verifyResult.getSubject();

            List<String> getAuthorities = verifyResult.getClaim("authorities").asList(String.class);

            List<GrantedAuthority> convertAuthorities = getAuthorities.stream().map(
                    result -> new SimpleGrantedAuthority(result)
            ).collect(Collectors.toList());

            UsernamePasswordAuthenticationToken springToken = new UsernamePasswordAuthenticationToken(
                    getId,
                    "(protected)",
                    convertAuthorities
            );

            return springToken;
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            authExceptionBody.setAuthException(exception.getMessage(), HttpStatus.UNAUTHORIZED.value(), path);
            return new UsernamePasswordAuthenticationToken(null, null, null);
        }
    }

    @Override
    public String verityRefreshToken(String refreshToken) {
        try {
            // get algorithm
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            // set verifier method for verify
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(isSuer).build(); //Reusable verifier instance

            // decoded
            DecodedJWT verifyResult = verifier.verify(refreshToken);

            // get id from sub
            String myId = verifyResult.getSubject();

            return myId;
        } catch(JWTVerificationException exception) {
            return "err" + exception.getMessage();
        }
    }
}
