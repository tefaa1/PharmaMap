package com.pharmamap.dto.pharmacy.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PharmacySlimResponse {

    private Long id;
    
    private String name;

    private String description;

    private String phone;

    private String address;
    
    private Double latitude;
    
    private Double longitude;
}
