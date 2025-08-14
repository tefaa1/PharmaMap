package com.spring.authservice.service;

import com.spring.authservice.dto.request.LoginDto;
import com.spring.authservice.dto.request.RegisterDto;
import com.spring.authservice.dto.response.AuthenticationResponseDto;

public interface AuthService {

    void register(RegisterDto registerDto);
    AuthenticationResponseDto login(LoginDto loginDto);
    void logout(String accessToken, String refreshToken);
}
