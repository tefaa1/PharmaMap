package com.pharmamap.service;

import com.pharmamap.dto.medicine.request.MedicineRequest;
import com.pharmamap.dto.medicine.request.MedicineUpdateRequest;
import com.pharmamap.dto.medicine.response.MedicineResponse;

public interface MedicineService {

	void createMedicine(MedicineRequest request);
	void updateMedicineById(MedicineUpdateRequest request,Long id);
	void deleteMedicineById(Long id);
	MedicineResponse getMedicineById(Long id);
}
