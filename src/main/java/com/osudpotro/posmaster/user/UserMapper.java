package com.osudpotro.posmaster.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //    @Mapping(target = "createdAt",expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);
    User toEntity(RegiterUserRequest request);
    @Mapping(target = "id",ignore = true)
    void update(UpdateUserRequest request,@MappingTarget User user);
}