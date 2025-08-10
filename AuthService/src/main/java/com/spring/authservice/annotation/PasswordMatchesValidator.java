package com.spring.authservice.annotation;

import com.spring.authservice.dto.request.RegisterDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterDto> {

    @Override
    public boolean isValid(RegisterDto registerDto, ConstraintValidatorContext constraintValidatorContext) {

        return registerDto.getPassword() != null&&
                registerDto.getConfirmPassword() !=null&&
                registerDto.getPassword().equals(registerDto.getConfirmPassword());
    }
}
