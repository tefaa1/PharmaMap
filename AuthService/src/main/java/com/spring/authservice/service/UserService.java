package com.spring.authservice.service;

import com.spring.authservice.dto.request.*;
import com.spring.authservice.dto.response.AuthenticationResponseDto;
import com.spring.authservice.dto.response.UserResponseDto;

public interface UserService {

    void register(RegisterDto registerDto);
    AuthenticationResponseDto login(LoginDto loginDto);
    void logout(String accessToken, String refreshToken);
    AuthenticationResponseDto refreshAccessToken(RefreshTokenDto refreshTokenDto);
    void changePassword (ChangePasswordDto changeDto);
    void changeTheName(ChangeNameDto changeName);
    void deleteTheCurrentUser(String accessToken, String refreshToken);
    UserResponseDto getTheCurrentUserInfo();
}
