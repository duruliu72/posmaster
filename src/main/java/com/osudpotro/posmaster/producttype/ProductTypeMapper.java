package com.osudpotro.posmaster.producttype;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductTypeMapper {
    ProductTypeDto toDto(ProductType productType);
    ProductType toEntity(ProductTypeCreateRequest request);
    void update(ProductTypeUpdateRequest request, @MappingTarget ProductType productType);
}