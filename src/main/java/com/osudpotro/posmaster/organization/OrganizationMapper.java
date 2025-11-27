package com.osudpotro.posmaster.organization;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    OrganizationDto toDto(Organization organization);
    Organization toEntity(OrganizationCreateRequest request);
    void update(OrganizationUpdateRequest request, @MappingTarget Organization organization);
}