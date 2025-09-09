package com.pharmamap.controller;

import com.pharmamap.dto.category.request.CategoryRequest;
import com.pharmamap.dto.category.response.CategoryResponse;
import com.pharmamap.dto.category.response.CategorySlimResponse;
import com.pharmamap.dto.medicine.response.MedicineResponse;
import com.pharmamap.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/pharmacy/{pharmacyId}")
    public ResponseEntity<CategorySlimResponse> create(@PathVariable Long pharmacyId,
                                                       @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request, pharmacyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getWithMedicines(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryWithMedicinesById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategorySlimResponse> update(@PathVariable Long id,
                                                       @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{targetCategoryId}/move/{medicineId}")
    public ResponseEntity<Void> moveMedicine(@PathVariable Long targetCategoryId, @PathVariable Long medicineId) {
        categoryService.moveMedicineToAnotherCategory(medicineId, targetCategoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{categoryId}/medicines/search")
    public ResponseEntity<List<MedicineResponse>> search(@PathVariable Long categoryId,
                                                         @RequestParam(name = "name", required = false) String name) {
        return ResponseEntity.ok(categoryService.searchMedicinesInCategory(categoryId, name));
    }
}
