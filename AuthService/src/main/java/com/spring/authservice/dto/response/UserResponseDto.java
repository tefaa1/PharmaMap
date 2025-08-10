package com.spring.authservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private String name;

    private String email;

    private String phone;
}
