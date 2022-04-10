package com.demo.firstProject.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.demo.firstProject.Exception.AuthException.AuthExceptionBody;
import com.demo.firstProject.Exception.BaseException;
import com.demo.firstProject.Service.ServiceModel.AccountService.Jsonwebtoken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JsonwebtokenService implements Jsonwebtoken {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.issuer}")
    private String isSuer;

    @Autowired
    private AuthExceptionBody authExceptionBody;


    @Override
    public String genAccessToken(String id, List<String> roles, String path) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            // generate access token
            return JWT.create()

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

                    // assign algorithm
                    .sign(algorithm);

        } catch (JWTCreationException exception){

            // create log error
            log.error("can't generate access token, message:{}", exception.getMessage());

            // throw error
            throw new BaseException(
                    "can't generate access token. because: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    path
            );
        }
    }

    @Override
    public String genRefreshToken(String id, String path) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            // generate refresh token
            return JWT.create()

                    // uuid
                    .withSubject(id)

                    // path of controller
                    .withIssuer(isSuer)

                    // date class init current time use millisecond and dplus 108,000,000 millisecon from 30 * 60 * 60 * 1000
                    // 30 hour
//                    .withExpiresAt(new Date(System.currentTimeMillis() + (30 * 60 * 60 * 1000)))

                    // 3 day
                    .withExpiresAt(new Date(System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000)))

                    // assign algorithm
                    .sign(algorithm);

        } catch (JWTCreationException exception) {

            // log error
            log.error("can't generate refresh token, message:{}", exception.getMessage());

            // throw error
            throw new BaseException(
                    "generate access token error. because: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    path
            );
        }
    }

    @Override
    public UsernamePasswordAuthenticationToken verifyToken(String jsonWebToken, String path) {
        try {
            // get algorithm
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            // set JWTVerifier class for verify
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(isSuer)
                    .build(); //Reusable verifier instance

            // decoded
            DecodedJWT verifyResult = verifier.verify(jsonWebToken);

            String getId = verifyResult.getSubject();

            List<String> getAuthorities = verifyResult.getClaim("authorities").asList(String.class);

            List<GrantedAuthority> convertAuthorities = getAuthorities.stream().map(
                    SimpleGrantedAuthority::new
            ).collect(Collectors.toList());

            UsernamePasswordAuthenticationToken springToken = new UsernamePasswordAuthenticationToken(
                    getId,
                    "(protected)",
                    convertAuthorities
            );

            return springToken;
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            //Invalid signature/claims

            // create log error
            log.error("verify access token error with:{}", exception.getMessage());

            // assign error to authExceptionBody
            authExceptionBody.setAuthException(exception.getMessage(), HttpStatus.UNAUTHORIZED.value(), path);

            // return null
            return new UsernamePasswordAuthenticationToken(null, null, null);
        }
    }

    @Override
    public String verityRefreshToken(String refreshToken, String path) {
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
            // log error
            log.error("can't generate refresh token, message:{}", exception.getMessage());

            // throw error
            throw new BaseException(
                    "generate access token error. because: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    path
            );
        }
    }
}
