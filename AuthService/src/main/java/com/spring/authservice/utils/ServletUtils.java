package com.spring.authservice.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class ServletUtils {

    public String extractAccessToken(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        return authHeader.substring(7);
    }

    public String extractRefreshToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        String refreshToken = "";

        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        return refreshToken;
    }
}
