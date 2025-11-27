package com.osudpotro.posmaster.variantunit;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VariantUnitMapper {
    VariantUnitDto toDto(VariantUnit variantUnit);
    VariantUnit toEntity(VariantUnitCreateRequest request);
    void update(VariantUnitUpdateRequest request, @MappingTarget VariantUnit variantUnit);
}