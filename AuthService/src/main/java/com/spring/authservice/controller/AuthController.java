package com.spring.authservice.controller;

import com.spring.authservice.dto.request.LoginDto;
import com.spring.authservice.dto.request.RegisterDto;
import com.spring.authservice.dto.response.ApiResponse;
import com.spring.authservice.dto.response.AuthenticationResponseDto;
import com.spring.authservice.security.JWTUtils;
import com.spring.authservice.service.AuthService;
import com.spring.authservice.utils.ServletUtils;
import jakarta.servlet.Servlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JWTUtils jwtUtils;
    private final ServletUtils servletUtils;

    @Autowired
    public AuthController(AuthService authService,
                          JWTUtils jwtUtils,
                          ServletUtils servletUtils) {

        this.authService = authService;
        this.jwtUtils = jwtUtils;
        this.servletUtils = servletUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDto registerDto) {

        authService.register(registerDto);
        return ResponseEntity.ok(
                new ApiResponse<>(null,
                        "successful register", HttpStatus.OK.value())
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {

        AuthenticationResponseDto authenticationResponseDto = authService.login(loginDto);

        response.setHeader("Authorization",
                "Bearer " + authenticationResponseDto.getAccessToken());

        String refreshToken = authenticationResponseDto.getRefreshToken();
        int refreshTokenExpiryInSeconds = (int) jwtUtils.getRemainingMillis(refreshToken) / 1000;

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        // cookie.setSecure(true);   in the production
        cookie.setPath("/");
        cookie.setMaxAge(refreshTokenExpiryInSeconds);
        response.addCookie(cookie);

        return ResponseEntity.ok(
                new ApiResponse<>(authenticationResponseDto,
                        "successful login", HttpStatus.OK.value())
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        String accessToken = servletUtils.extractAccessToken(request);
        String refreshToken = servletUtils.extractRefreshToken(request);

        authService.logout(accessToken, refreshToken);
        return ResponseEntity.ok(
                new ApiResponse<>(null,
                        "successful login", HttpStatus.OK.value())
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = servletUtils.extractRefreshToken(request);
        String accessToken = authService.refreshAccessToken(refreshToken);

        response.setHeader("Authorization", "Bearer " + accessToken);
        return ResponseEntity.ok(
                new ApiResponse<>(null,
                        "successful refresh", HttpStatus.OK.value())
        );
    }
}
