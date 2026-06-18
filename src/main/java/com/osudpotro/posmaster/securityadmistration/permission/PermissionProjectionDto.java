package com.osudpotro.posmaster.securityadmistration.permission;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PermissionProjectionDto {
    private Long permissionId;
    private String resourceName;
    private Long moduleId;
    private String moduleName;
    private Long resourceId;
    private List<PermissionActionProjectionDto> permissionActions = new ArrayList<>();

}
