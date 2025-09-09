package com.pharmamap.service;

import com.pharmamap.dto.category.request.CategoryRequest;
import com.pharmamap.dto.category.response.CategoryResponse;
import com.pharmamap.dto.category.response.CategorySlimResponse;
import com.pharmamap.dto.medicine.response.MedicineResponse;
import com.pharmamap.entity.Category;
import com.pharmamap.entity.Medicine;
import com.pharmamap.entity.Pharmacy;
import com.pharmamap.mapper.CategoryMapper;
import com.pharmamap.mapper.MedicineMapper;
import com.pharmamap.repository.CategoryRepository;
import com.pharmamap.repository.MedicineRepository;
import com.pharmamap.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final PharmacyRepository pharmacyRepository;
    private final MedicineRepository medicineRepository;
    private final CategoryMapper categoryMapper;
    private final MedicineMapper medicineMapper;

    @Override
    public CategorySlimResponse createCategory(CategoryRequest request, Long pharmacyId) {
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new RuntimeException("Pharmacy not found with id: " + pharmacyId));
        
        Category category = categoryMapper.toEntity(request);
        category.setPharmacy(pharmacy);
        
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toSlimResponse(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryWithMedicinesById(Long id) {
        Category category = categoryRepository.findCategoryByIdWithMedicines(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return categoryMapper.toResponse(category);
    }

    @Override
    public CategorySlimResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toSlimResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public void moveMedicineToAnotherCategory(Long medicineId, Long targetCategoryId) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + medicineId));

        Category targetCategory = categoryRepository.findById(targetCategoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + targetCategoryId));

        Category currentCategory = medicine.getCategory();
        if (!currentCategory.getPharmacy().getId().equals(targetCategory.getPharmacy().getId())) {
            throw new RuntimeException("Cannot move medicine between different pharmacies");
        }
        if(currentCategory.getId().equals(targetCategoryId)) {
            throw new RuntimeException("Cannot move medicine to the same category");
        }

        medicine.setCategory(targetCategory);
        medicineRepository.save(medicine);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineResponse> searchMedicinesInCategory(Long categoryId, String name) {
        return medicineRepository.findByCategoryIdAndNameContainingIgnoreCase(categoryId, name == null ? "" : name)
                .stream()
                .map(medicineMapper::toResponse)
                .collect(Collectors.toList());
    }
}
