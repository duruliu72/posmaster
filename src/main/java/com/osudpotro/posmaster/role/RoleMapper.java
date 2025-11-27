package com.osudpotro.posmaster.role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto toDto(Role role);
    Role toEntity(RoleCreateRequest request);
    void update(RoleUpdateRequest request, @MappingTarget Role role);
}
