package com.spring.authservice.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.spring.authservice.dto.request.*;
import com.spring.authservice.dto.response.AuthenticationResponseDto;
import com.spring.authservice.dto.response.UserResponseDto;
import com.spring.authservice.repository.UserRepository;
import com.spring.authservice.security.JWTUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    public UserServiceImpl(UserRepository userRepository,
                           AuthenticationManager authenticationManager,
                           JWTUtils jwtUtils){
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }
    @Override
    public void register(RegisterDto registerDto) {


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
    public AuthenticationResponseDto refreshAccessToken(RefreshTokenDto refreshTokenDto) {

        if(!jwtUtils.isTokenValid(refreshTokenDto.getRefreshToken())){
            throw new JWTVerificationException("Token expired or invalid");
        }

        if(jwtUtils.isAccessToken(refreshTokenDto.getRefreshToken())){
            throw new JWTVerificationException("Access token is not allowed here");
        }

        return null;
    }

    @Override
    public void changePassword(ChangePasswordDto changeDto) {

    }

    @Override
    public void changeTheName(ChangeNameDto changeName) {

    }

    @Override
    public UserResponseDto getTheCurrentUser() {
        return null;
    }

    @Override
    public void deleteTheCurrentUser() {

    }
}
