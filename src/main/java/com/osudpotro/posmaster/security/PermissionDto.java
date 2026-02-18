package com.osudpotro.posmaster.security;


import com.osudpotro.posmaster.resource.ResourceDto;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class PermissionDto {
    private ResourceDto resource;
    private PermissionType permissionType;
    private Set<PermissionDetailDto> permissionDetails = new HashSet<>();
}
