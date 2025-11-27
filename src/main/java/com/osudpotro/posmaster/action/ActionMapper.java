package com.osudpotro.posmaster.action;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ActionMapper {
    ActionDto toDto(Action action);
    Action toEntity(ActionCreateRequest request);
    void update(UpdateActionRequest request, @MappingTarget Action action);
}