package com.osudpotro.posmaster.supplier;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    SupplierDto toDto(Supplier supplier);
    Supplier toEntity(SupplierCreateRequest request);
    void update(SupplierUpdateRequest request, @MappingTarget Supplier supplier);
}