package com.pharmamap.mapper;

import com.pharmamap.dto.category.request.CategoryRequest;
import com.pharmamap.dto.category.response.CategoryResponse;
import com.pharmamap.dto.category.response.CategorySlimResponse;
import com.pharmamap.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {MedicineMapper.class})
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pharmacy", ignore = true)
    @Mapping(target = "medicineSet", ignore = true)
    Category toEntity(CategoryRequest request);

    @Mapping(target = "medicineSet", source = "medicineSet")
    CategoryResponse toResponse(Category category);

    CategorySlimResponse toSlimResponse(Category category);
}
