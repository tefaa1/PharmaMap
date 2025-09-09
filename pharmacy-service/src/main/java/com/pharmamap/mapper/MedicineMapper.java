package com.pharmamap.mapper;

import com.pharmamap.dto.medicine.request.MedicineRequest;
import com.pharmamap.dto.medicine.request.MedicineUpdateRequest;
import com.pharmamap.dto.medicine.response.MedicineResponse;
import com.pharmamap.entity.Medicine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MedicineMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "category", ignore = true)
	Medicine toEntity(MedicineRequest request);

	MedicineResponse toResponse(Medicine medicine);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "category", ignore = true)
	void updateEntity(MedicineUpdateRequest request, @MappingTarget Medicine medicine);
}
