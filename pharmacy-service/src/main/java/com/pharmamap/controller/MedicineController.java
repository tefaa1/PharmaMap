package com.pharmamap.controller;

import com.pharmamap.dto.medicine.request.MedicineRequest;
import com.pharmamap.dto.medicine.request.MedicineUpdateRequest;
import com.pharmamap.dto.medicine.response.MedicineResponse;
import com.pharmamap.service.MedicineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody MedicineRequest request) {
        medicineService.createMedicine(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(medicineService.getMedicineById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody MedicineUpdateRequest request) {
        medicineService.updateMedicineById(request, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicineService.deleteMedicineById(id);
        return ResponseEntity.noContent().build();
    }
}
