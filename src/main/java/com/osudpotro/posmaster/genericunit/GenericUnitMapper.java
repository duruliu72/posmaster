package com.osudpotro.posmaster.genericunit;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GenericUnitMapper {
    GenericUnitDto toDto(GenericUnit genericUnit);
    GenericUnit toEntity(GenericUnitCreateRequest request);
    void update(GenericUnitUpdateRequest request, @MappingTarget GenericUnit genericUnit);
}