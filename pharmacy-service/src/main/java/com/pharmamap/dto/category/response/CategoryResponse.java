package com.pharmamap.dto.category.response;

import com.pharmamap.dto.medicine.response.MedicineResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private Long id;

    private String name;

    private String description;

    private Set<MedicineResponse>medicineSet;
}
