package com.osudpotro.posmaster.generic;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GenericMapper {
    GenericDto toDto(Generic generic);
    Generic toEntity(GenericCreateRequest request);
    void update(GenericUpdateRequest request, @MappingTarget Generic generic);
}