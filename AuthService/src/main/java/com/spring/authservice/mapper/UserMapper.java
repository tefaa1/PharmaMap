package com.spring.authservice.mapper;

import com.spring.authservice.dto.request.RegisterDto;
import com.spring.authservice.dto.response.UserResponseDto;
import com.spring.authservice.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toDto(User user);

    User toEntity(RegisterDto registerDto);
}
