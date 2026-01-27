package com.osudpotro.posmaster.multimedia;


import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MultimediaMapper {
    MultimediaDto toDto(Multimedia multimedia);
    Multimedia toEntity(MultimediaCreateRequest request);
    void update(MultimediaUpdateRequest request, @MappingTarget Multimedia multimedia);
}
