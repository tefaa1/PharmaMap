package com.pharmamap.service;

import com.pharmamap.dto.medicine.request.MedicineRequest;
import com.pharmamap.dto.medicine.request.MedicineUpdateRequest;
import com.pharmamap.dto.medicine.response.MedicineResponse;
import com.pharmamap.entity.Category;
import com.pharmamap.entity.Medicine;
import com.pharmamap.mapper.MedicineMapper;
import com.pharmamap.repository.CategoryRepository;
import com.pharmamap.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicineServiceImpl implements MedicineService{

    private final MedicineRepository medicineRepository;
    private final CategoryRepository categoryRepository;
    private final MedicineMapper medicineMapper;

    @Override
    public void createMedicine(MedicineRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));
        
        Medicine medicine = medicineMapper.toEntity(request);
        medicine.setCategory(category);
        
        medicineRepository.save(medicine);
    }

    @Override
    public void updateMedicineById(MedicineUpdateRequest request, Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));
        
        // Apply partial updates from request (nulls ignored by mapper)
        medicineMapper.updateEntity(request, medicine);

        medicineRepository.save(medicine);
    }

    @Override
    public void deleteMedicineById(Long id) {
        if (!medicineRepository.existsById(id)) {
            throw new RuntimeException("Medicine not found with id: " + id);
        }
        medicineRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineResponse getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));
        return medicineMapper.toResponse(medicine);
    }

}
