package com.osudpotro.posmaster.category;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryCreateRequest request);

    void update(CategoryUpdateRequest request, @MappingTarget Category category);
}
