package com.osudpotro.posmaster.securityadmistration.permission;


import com.osudpotro.posmaster.securityadmistration.resource.ResourceDto;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class PermissionDto {
    private ResourceDto resource;
    private PermissionType permissionType;
    private Set<PermissionActionDto> permissionActions = new HashSet<>();
}
