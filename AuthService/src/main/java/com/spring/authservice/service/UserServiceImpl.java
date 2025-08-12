package com.spring.authservice.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.spring.authservice.dto.request.*;
import com.spring.authservice.dto.response.AuthenticationResponseDto;
import com.spring.authservice.dto.response.UserResponseDto;
import com.spring.authservice.mapper.UserMapper;
import com.spring.authservice.model.User;
import com.spring.authservice.repository.UserRepository;
import com.spring.authservice.security.JWTUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
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
    public AuthenticationResponseDto refreshAccessToken(RefreshTokenDto refreshTokenDto) {

        String refreshToken = refreshTokenDto.getRefreshToken();

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
        String accessToken = jwtUtils.generateAccessToken(email);

        return new AuthenticationResponseDto(accessToken,refreshToken);
    }

    @Override
    public void changePassword(ChangePasswordDto changeDto) {
        User user = getTheCurrentUser();

        if(!passwordEncoder.matches(changeDto.getOldPassword(),user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(changeDto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void changeTheName(ChangeNameDto changeName) {

        User user = getTheCurrentUser();

        user.setName(changeName.getName());
        userRepository.save(user);
    }

    @Override
    public void deleteTheCurrentUser(String accessToken, String refreshToken) {

        jwtUtils.blacklistToken(accessToken);
        jwtUtils.blacklistToken(refreshToken);

        User user = getTheCurrentUser();
        userRepository.delete(user);
    }

    @Override
    public UserResponseDto getTheCurrentUserInfo() {

        User user = getTheCurrentUser();
        return userMapper.toDto(user);
    }

    private User getTheCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return getTheUserByEmail(email);
    }

    private User getTheUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No user with this email"));
    }
}
