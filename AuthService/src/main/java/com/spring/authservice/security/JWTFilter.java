package com.spring.authservice.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.spring.authservice.repository.BlacklistedTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final BlacklistedTokenRepository tokenBlacklistRepository;
    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public JWTFilter(JWTUtils jwtUtils,
                     BlacklistedTokenRepository tokenBlacklistRepository,
                     MyUserDetailsService myUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("before");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            System.out.println("after_if");
            String jwt = authHeader.substring(7);

            try {
                if (!jwtUtils.isTokenValid(jwt)) {
                    throw new JWTVerificationException("Token is expired or invalid");
                }
                System.out.println("after_if2");
                if (tokenBlacklistRepository.existsByToken(jwt)) {
                    throw new JWTVerificationException("Token is blacklisted");
                }
                System.out.println("after_if3");
                String email = jwtUtils.extractEmail(jwt);

                UserDetails userDetails = myUserDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
                System.out.println("after_if4");
            } catch (TokenExpiredException ex) {
                throw new AuthenticationServiceException("JWT token expired");
            } catch (JWTVerificationException ex) {
                throw new AuthenticationServiceException("Invalid JWT token");
            } catch (Exception ex) {
                throw new AuthenticationServiceException("Authentication error");
            }
        }
        filterChain.doFilter(request, response);
    }
}
