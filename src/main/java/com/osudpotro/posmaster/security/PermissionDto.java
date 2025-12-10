package com.osudpotro.posmaster.security;


import com.osudpotro.posmaster.resource.api.ApiResourceDto;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class PermissionDto {
    private ApiResourceDto apiResource;
    private PermissionType permissionType;
    private Set<PermissionDetailDto> permissionDetails = new HashSet<>();
}
