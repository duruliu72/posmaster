package com.osudpotro.posmaster.varianttype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VariantTypeMapper {
    VariantTypeDto toDto(VariantType variantType);
    VariantType toEntity(VariantTypeCreateRequest request);
    void update(VariantTypeUpdateRequest request, @MappingTarget VariantType variantType);
}
