package com.karnty.training.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.karnty.training.backend.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class TokenService {

    @Value("${app.token.secret}")
    private String secret;
    @Value("${app.token.issuer}")
    private String issuer;
    public String tokenize(User user){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,60);
        Date expiresAt = calendar.getTime();

        return JWT.create()
                .withIssuer(issuer)
                .withClaim("principal", user.getId())
                .withClaim("role","USER")
                .withExpiresAt(expiresAt)
                .sign(getAlgorithm());
    }

    public DecodedJWT verify(String token){
        try{
            return JWT.require(getAlgorithm())
                    .withIssuer(issuer)
                    .build()
                    .verify(token);
        }catch(Exception e){
            return null;
        }
    }
    public Algorithm getAlgorithm(){
        return Algorithm.HMAC256(secret);
    }
}
