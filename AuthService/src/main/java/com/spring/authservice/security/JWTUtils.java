package com.spring.authservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.spring.authservice.model.BlacklistedToken;
import com.spring.authservice.repository.BlacklistedTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JWTUtils {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public JWTUtils(BlacklistedTokenRepository blacklistedTokenRepository){
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

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

    public void blacklistToken(String token){

        BlacklistedToken blacklistedToken = BlacklistedToken
                .builder()
                .token(token)
                .build();

        blacklistedTokenRepository.save(blacklistedToken);
    }

    public Boolean isTokenBlacklisted(String token){

        DecodedJWT jwt = getVerifier().verify(token);
        return blacklistedTokenRepository.existsByToken(token);
    }

    public Boolean isAccessToken(String token){

        DecodedJWT jwt = getVerifier().verify(token);
        return jwt.getSubject().equals("access");
    }

    private JWTVerifier getVerifier() throws JWTVerificationException {

        return JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("PharmaMap")
                .build();
    }
}
