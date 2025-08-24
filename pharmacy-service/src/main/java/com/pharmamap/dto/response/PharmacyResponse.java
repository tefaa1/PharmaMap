package com.pharmamap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyResponse {

    private Long id;
    
    private String name;

    private String description;

    private String phone;

    private String address;

    Set<CategoryResponse> categorySet;
}
