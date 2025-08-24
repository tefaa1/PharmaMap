package com.pharmamap.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineRequest {

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    @Positive
    private double price;

    @NotBlank
    @PositiveOrZero
    private Integer stockQuantity;

    @NotBlank
    private LocalDate expireAt;

    @NotBlank
    private Long categoryId;
}
