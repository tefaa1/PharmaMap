package com.spring.authservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JWTUtils {

    @Value("${jwt_secret}")
    private String secret;

    public String generateAccessToken(String email) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject("access")
                .withClaim("email", email)
                .withIssuer("PharmaMap")
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
                .sign(Algorithm.HMAC256(secret));
    }

    public String generateRefreshToken(String email) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject("refresh")
                .withClaim("email", email)
                .withIssuer("PharmaMap")
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
                .sign(Algorithm.HMAC256(secret));
    }

    public String extractEmail(String token){

        DecodedJWT jwt = getVerifier().verify(token);
        return jwt.getClaim("email").asString();
    }

    public Boolean isTokenValid(String token) {

        DecodedJWT jwt =  getVerifier().verify(token);
        return jwt.getExpiresAt().after(new Date());
    }

    public Boolean isAccessToken(String token){

        DecodedJWT jwt = getVerifier().verify(token);
        return jwt.getSubject().equals("access");
    }

    public long getRemainingMillis(String token){

        DecodedJWT jwt = getVerifier().verify(token);
        return jwt.getExpiresAt().getTime() - System.currentTimeMillis();
    }

    private JWTVerifier getVerifier() throws JWTVerificationException {

        return JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("PharmaMap")
                .build();
    }
}
