package com.pharmamap.controller;

import com.pharmamap.dto.pharmacy.request.PharmacyRequest;
import com.pharmamap.dto.pharmacy.request.PharmacyUpdateRequest;
import com.pharmamap.dto.pharmacy.response.PharmacyResponse;
import com.pharmamap.dto.pharmacy.response.PharmacySlimResponse;
import com.pharmamap.dto.pharmacy.response.PharmacyWithSlimCategoriesResponse;
import com.pharmamap.service.PharmacyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacies")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    public PharmacyController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    @PostMapping
    public ResponseEntity<PharmacySlimResponse> create(@Valid @RequestBody PharmacyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pharmacyService.createPharmacy(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PharmacyWithSlimCategoriesResponse> getWithCategories(@PathVariable("id") Long id) {
        return ResponseEntity.ok(pharmacyService.getPharmacyWithCategoriesById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<List<PharmacySlimResponse>> myPharmacies() {
        return ResponseEntity.ok(pharmacyService.getTheSlimePharmaciesOfTheCuUser());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PharmacyResponse> update(@PathVariable("id") Long id,
                                                   @Valid @RequestBody PharmacyUpdateRequest request) {
        return ResponseEntity.ok(pharmacyService.updatePharmacy(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        pharmacyService.deletePharmacy(id);
        return ResponseEntity.noContent().build();
    }
}
