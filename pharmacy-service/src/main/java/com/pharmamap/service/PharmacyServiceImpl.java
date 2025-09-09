package com.pharmamap.service;

import com.pharmamap.dto.pharmacy.request.PharmacyRequest;
import com.pharmamap.dto.pharmacy.request.PharmacyUpdateRequest;
import com.pharmamap.dto.pharmacy.response.PharmacyResponse;
import com.pharmamap.dto.pharmacy.response.PharmacySlimResponse;
import com.pharmamap.dto.pharmacy.response.PharmacyWithSlimCategoriesResponse;
import com.pharmamap.entity.Pharmacy;
import com.pharmamap.mapper.PharmacyMapper;
import com.pharmamap.repository.PharmacyRepository;
import com.pharmamap.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyRepository pharmacyRepository;
    private final PharmacyMapper pharmacyMapper;

    @Override
    public PharmacySlimResponse createPharmacy(PharmacyRequest request) {
        Pharmacy pharmacy = pharmacyMapper.toEntity(request);
        Pharmacy savedPharmacy = pharmacyRepository.save(pharmacy);
        return pharmacyMapper.toSlimResponse(savedPharmacy);
    }

    @Override
    @Transactional(readOnly = true)
    public PharmacyWithSlimCategoriesResponse getPharmacyWithCategoriesById(Long pharmacyId) {
        Pharmacy pharmacy = pharmacyRepository.findPharmacyByIdWithCategories(pharmacyId)
                .orElseThrow(() -> new RuntimeException("Pharmacy not found with id: " + pharmacyId));
        return pharmacyMapper.toWithSlimCategoriesResponse(pharmacy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacySlimResponse> getTheSlimePharmaciesOfTheCuUser() {

        String userId = UserUtil.getCurrentUserId();
        List<Pharmacy> pharmacyList = pharmacyRepository.findByUserId(userId);

        return pharmacyList.stream()
                .map(pharmacyMapper::toSlimResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PharmacyResponse updatePharmacy(Long id, PharmacyUpdateRequest request) {
        Pharmacy pharmacy = pharmacyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pharmacy not found with id: " + id));

        String userId = UserUtil.getCurrentUserId();
        if(!pharmacy.getUserId().equals(userId)){
            throw new RuntimeException("Pharmacy with id "+ id + " is not you pharmacy");
        }
        
        pharmacyMapper.updatePharmacyFromRequest(request, pharmacy);
        Pharmacy updatedPharmacy = pharmacyRepository.save(pharmacy);
        return pharmacyMapper.toResponse(updatedPharmacy);
    }

    @Override
    public void deletePharmacy(Long id) {
        Pharmacy pharmacy = pharmacyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pharmacy not found with id: " + id));

        String userId = UserUtil.getCurrentUserId();
        if(!pharmacy.getUserId().equals(userId)){
            throw new RuntimeException("Pharmacy with id "+ id + " is not you pharmacy");
        }

        pharmacyRepository.deleteById(id);
    }
}
