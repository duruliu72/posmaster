package com.osudpotro.posmaster.tag;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDto toDto(Tag tag);
    Tag toEntity(TagCreateRequest request);
    void update(TagUpdateRequest request, @MappingTarget Tag tag);
}
