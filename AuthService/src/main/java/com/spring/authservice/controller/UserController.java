package com.spring.authservice.controller;

import com.spring.authservice.dto.request.ChangeNameDto;
import com.spring.authservice.dto.request.ChangePasswordDto;
import com.spring.authservice.dto.response.ApiResponse;
import com.spring.authservice.dto.response.UserResponseDto;
import com.spring.authservice.service.UserService;
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
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final ServletUtils servletUtils;

    @Autowired
    public UserController(UserService userService,
                          ServletUtils servletUtils){
        this.userService = userService;
        this.servletUtils = servletUtils;
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?>changePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto){

        userService.changePassword(changePasswordDto);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        null,"Password changed successfully", HttpStatus.OK.value()
                )
        );
    }

    @PatchMapping("/change-name")
    public ResponseEntity<?>changeName(@RequestBody @Valid ChangeNameDto changeNameDto){

        userService.changeTheName(changeNameDto);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        null,"Name changed successfully", HttpStatus.OK.value()
                )
        );
    }

    @DeleteMapping("/delete-my-account")
    public ResponseEntity<?>deleteCurrentUser(HttpServletRequest request,
                                                 HttpServletResponse response) {

        String accessToken = servletUtils.extractAccessToken(request);
        String refreshToken = servletUtils.extractRefreshToken(request);

        userService.deleteTheCurrentUser(accessToken,refreshToken);

        // Invalidate refresh token cookie by setting same name and MaxAge=0
        Cookie cookie = new Cookie("refreshToken","");
        cookie.setHttpOnly(true);
        // cookie.setSecure(true);   in the production
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        null,"Account deleted successfully", HttpStatus.OK.value()
                )
        );
    }

    @GetMapping
    public ResponseEntity<?>getCurrentUserInfo(){

        UserResponseDto userResponseDto = userService.getTheCurrentUserInfo();
        return ResponseEntity.ok(
                new ApiResponse<>(
                        userResponseDto,"User data retrieved successfully",HttpStatus.OK.value()
                )
        );
    }
}
