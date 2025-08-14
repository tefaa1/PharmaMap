package com.spring.authservice.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.spring.authservice.dto.request.LoginDto;
import com.spring.authservice.dto.request.RefreshTokenDto;
import com.spring.authservice.dto.request.RegisterDto;
import com.spring.authservice.dto.response.AuthenticationResponseDto;
import com.spring.authservice.mapper.UserMapper;
import com.spring.authservice.model.User;
import com.spring.authservice.repository.UserRepository;
import com.spring.authservice.security.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           AuthenticationManager authenticationManager,
                           JWTUtils jwtUtils,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(RegisterDto registerDto) {

        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use");
        }

        User user = userMapper.toEntity(registerDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public AuthenticationResponseDto login(LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authToken
                = new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword());

        authenticationManager.authenticate(authToken);

        String accessToken = jwtUtils.generateAccessToken(loginDto.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(loginDto.getEmail());

        return AuthenticationResponseDto
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String accessToken, String refreshToken) {

        jwtUtils.blacklistToken(accessToken);
        jwtUtils.blacklistToken(refreshToken);
    }

    @Override
    public String refreshAccessToken(String refreshToken) {

        if(!jwtUtils.isTokenValid(refreshToken)){
            throw new JWTVerificationException("Token expired or invalid");
        }

        if(jwtUtils.isAccessToken(refreshToken)){
            throw new JWTVerificationException("Access token is not allowed here");
        }

        if(jwtUtils.isTokenBlacklisted(refreshToken)){
            throw new JWTVerificationException("Token is Blacklisted");
        }

        String email = jwtUtils.extractEmail(refreshToken);

        return jwtUtils.generateAccessToken(email);
    }
}
