package com.spring.authservice.service;

import com.spring.authservice.dto.request.*;
import com.spring.authservice.dto.response.AuthenticationResponseDto;
import com.spring.authservice.dto.response.UserResponseDto;
import com.spring.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public void register(RegisterDto registerDto) {
        
    }

    @Override
    public AuthenticationResponseDto login(LoginDto loginDto) {
        return null;
    }

    @Override
    public AuthenticationResponseDto refreshAccessToken(RefreshTokenDto refreshTokenDto) {
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
