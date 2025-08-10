package com.spring.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
