package com.pharmamap.dto.pharmacy.response;

import com.pharmamap.dto.category.response.CategoryResponse;
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

    private Set<CategoryResponse> categorySet;
}
