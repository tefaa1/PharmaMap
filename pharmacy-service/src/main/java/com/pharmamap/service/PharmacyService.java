package com.pharmamap.service;

import com.pharmamap.dto.pharmacy.request.PharmacyRequest;
import com.pharmamap.dto.pharmacy.request.PharmacyUpdateRequest;
import com.pharmamap.dto.pharmacy.response.PharmacyResponse;
import com.pharmamap.dto.pharmacy.response.PharmacySlimResponse;
import com.pharmamap.dto.pharmacy.response.PharmacyWithSlimCategoriesResponse;

import java.util.List;

public interface PharmacyService {

    PharmacySlimResponse createPharmacy(PharmacyRequest request);

    PharmacyWithSlimCategoriesResponse getPharmacyWithCategoriesById(Long pharmacyId);
    
    List<PharmacySlimResponse> getTheSlimePharmaciesOfTheCuUser();
    
    PharmacyResponse updatePharmacy(Long id, PharmacyUpdateRequest request);
    
    void deletePharmacy(Long id);
}
