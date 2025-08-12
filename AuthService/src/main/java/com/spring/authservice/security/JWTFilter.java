package com.spring.authservice.security;

import ch.qos.logback.core.util.StringUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.spring.authservice.repository.TokenBlacklistRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Autowired
    public JWTFilter(JWTUtils jwtUtils,
                     TokenBlacklistRepository tokenBlacklistRepository){
        this.jwtUtils = jwtUtils;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(authHeader)&&authHeader.startsWith("Bearer ")){

            String jwt = authHeader.substring(7);

            try {
                if(!jwtUtils.isTokenValid(jwt)){
                    throw new JWTVerificationException("Token is expired or invalid");
                }

                if(tokenBlacklistRepository.existsByToken(jwt)){
                    throw new JWTVerificationException("Token is blacklisted");
                }

                String email = jwtUtils.extractEmail(jwt);

                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(email,null);

                if(SecurityContextHolder.getContext().getAuthentication() == null){
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            catch (JWTVerificationException ex){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid or Expired JWT Token");
            }
        }
        filterChain.doFilter(request,response);
    }
}
