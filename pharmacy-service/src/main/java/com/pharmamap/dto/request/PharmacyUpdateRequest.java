package com.pharmamap.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyUpdateRequest {

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

}
