package com.osudpotro.posmaster.resource.api;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ApiResourceMapper {
    ApiResourceDto toDto(ApiResource apiResource);
    ApiResource toEntity(ApiResourceCreateRequest request);
    void update(ApiResourceUpdateRequest apiRequest, @MappingTarget ApiResource apiResource);
}