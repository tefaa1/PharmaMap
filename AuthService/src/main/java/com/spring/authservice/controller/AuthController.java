package com.spring.authservice.controller;

import com.spring.authservice.dto.request.LoginDto;
import com.spring.authservice.dto.request.RegisterDto;
import com.spring.authservice.dto.response.ApiResponse;
import com.spring.authservice.dto.response.AuthenticationResponseDto;
import com.spring.authservice.security.JWTUtils;
import com.spring.authservice.service.AuthService;
import com.spring.authservice.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JWTUtils jwtUtils;

    @Autowired
    public AuthController(AuthService authService,
                          JWTUtils jwtUtils){

        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    ResponseEntity<?>register(@RequestBody @Valid RegisterDto registerDto){

        authService.register(registerDto);
        return ResponseEntity.ok(
                new ApiResponse<>(null,
                        "successful register", HttpStatus.OK.value())
        );
    }

    @PostMapping("/login")
    ResponseEntity<?>login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {

        AuthenticationResponseDto authenticationResponseDto = authService.login(loginDto);

        response.setHeader("Authorization",
                "Bearer " + authenticationResponseDto.getAccessToken());

        String refreshToken = authenticationResponseDto.getRefreshToken();
        int refreshTokenExpiryInSeconds = (int) jwtUtils.getRemainingMillis(refreshToken) / 1000;

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(refreshTokenExpiryInSeconds);
        response.addCookie(cookie);

        return ResponseEntity.ok(
                new ApiResponse<>(authenticationResponseDto,
                        "successful login", HttpStatus.OK.value())
        );
    }

    @PostMapping("/logout")
    ResponseEntity<?>logout(HttpServletRequest request){

        String accessToken = extractAccessToken(request);
        String refreshToken = extractRefreshToken(request);

        authService.logout(accessToken,refreshToken);
        return ResponseEntity.ok(
                new ApiResponse<>(null,
                        "successful login",HttpStatus.OK.value())
        );
    }
    private String extractAccessToken(HttpServletRequest request){

        String authHeader = request.getHeader("Authorization");
        return authHeader.substring(7);
    }

    private String extractRefreshToken(HttpServletRequest request){

        Cookie[]cookies = request.getCookies();
        String refreshToken = "";

        for (Cookie cookie : cookies) {
            if("refreshToken".equals(cookie.getName())){
                refreshToken = cookie.getValue();
                break;
            }
        }

        return refreshToken;
    }
}
