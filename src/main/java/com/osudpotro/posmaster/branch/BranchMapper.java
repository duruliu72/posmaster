package com.osudpotro.posmaster.branch;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    BranchDto toDto(Branch resource);
    Branch toEntity(BranchCreateRequest request);
    void update(BranchUpdateRequest request, @MappingTarget Branch branch);
}