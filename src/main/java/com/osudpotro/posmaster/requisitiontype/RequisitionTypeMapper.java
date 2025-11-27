package com.osudpotro.posmaster.requisitiontype;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RequisitionTypeMapper {
    RequisitionTypeDto toDto(RequisitionType requisitionType);
    RequisitionType toEntity(RequisitionTypeCreateRequest request);
    void update(RequisitionTypeUpdateRequest request, @MappingTarget RequisitionType requisitionType);
}
