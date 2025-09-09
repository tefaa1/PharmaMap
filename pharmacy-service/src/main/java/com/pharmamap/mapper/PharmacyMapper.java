package com.pharmamap.mapper;

import com.pharmamap.dto.pharmacy.request.PharmacyRequest;
import com.pharmamap.dto.pharmacy.request.PharmacyUpdateRequest;
import com.pharmamap.dto.pharmacy.response.PharmacyResponse;
import com.pharmamap.dto.pharmacy.response.PharmacySlimResponse;
import com.pharmamap.dto.pharmacy.response.PharmacyWithSlimCategoriesResponse;
import com.pharmamap.entity.Pharmacy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {CategoryMapper.class})
public interface PharmacyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categorySet", ignore = true)
    Pharmacy toEntity(PharmacyRequest request);

    @Mapping(target = "categorySet", source = "categorySet")
    PharmacyResponse toResponse(Pharmacy pharmacy);

    @Mapping(target = "categorySet", source = "categorySet")
    PharmacyWithSlimCategoriesResponse toWithSlimCategoriesResponse(Pharmacy pharmacy);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categorySet", ignore = true)
    void updatePharmacyFromRequest(PharmacyUpdateRequest request, @MappingTarget Pharmacy pharmacy);

    PharmacySlimResponse toSlimResponse(Pharmacy pharmacy);
}
