package com.osudpotro.posmaster.manufacturer;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ManufacturerMapper {
    ManufacturerDto toDto(Manufacturer manufacturer);
    Manufacturer toEntity(ManufacturerCreateRequest request);
    void update(ManufacturerUpdateRequest request, @MappingTarget Manufacturer manufacturer);
}