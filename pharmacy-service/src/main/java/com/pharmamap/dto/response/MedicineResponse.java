package com.pharmamap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineResponse {

    private Long id;

    private String name;

    private String description;

    private double price;

    private Integer stockQuantity;

    private LocalDate expireAt;
}
