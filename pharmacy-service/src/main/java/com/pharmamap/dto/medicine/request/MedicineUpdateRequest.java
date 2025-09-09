package com.pharmamap.dto.medicine.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineUpdateRequest {

    private String name;

    private String description;

    @Positive
    private Double price;

    @PositiveOrZero
    private Integer stockQuantity;

    private LocalDate expireAt;
}
