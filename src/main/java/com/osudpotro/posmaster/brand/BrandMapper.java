package com.osudpotro.posmaster.brand;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandDto toDto(Brand brand);
    Brand toEntity(BrandCreateRequest request);
    void update(BrandUpdateRequest request, @MappingTarget Brand brand);
}