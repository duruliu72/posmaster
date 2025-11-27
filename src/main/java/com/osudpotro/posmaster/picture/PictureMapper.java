package com.osudpotro.posmaster.picture;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PictureMapper {
    PictureDto toDto(Picture picture);
    Picture toEntity(PictureCreateRequest request);
    void update(PictureUpdateRequest request, @MappingTarget Picture picture);
}
