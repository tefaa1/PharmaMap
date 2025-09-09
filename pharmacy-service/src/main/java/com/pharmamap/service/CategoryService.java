package com.pharmamap.service;

import com.pharmamap.dto.category.request.CategoryRequest;
import com.pharmamap.dto.category.response.CategoryResponse;
import com.pharmamap.dto.category.response.CategorySlimResponse;
import com.pharmamap.dto.medicine.response.MedicineResponse;

import java.util.List;

public interface CategoryService {

	CategorySlimResponse createCategory(CategoryRequest request, Long pharmacyId);
	
	CategoryResponse getCategoryWithMedicinesById(Long id);
	
	CategorySlimResponse updateCategory(Long id, CategoryRequest request);
	
	void deleteCategory(Long id);
	
	void moveMedicineToAnotherCategory(Long medicineId, Long targetCategoryId);
	
	List<MedicineResponse> searchMedicinesInCategory(Long categoryId, String name);
}
