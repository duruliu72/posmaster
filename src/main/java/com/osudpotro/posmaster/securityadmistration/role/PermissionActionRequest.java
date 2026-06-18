package com.osudpotro.posmaster.securityadmistration.role;

import lombok.Data;

@Data
public class PermissionActionRequest {
    private Long permissionId;
//    private Long roleId;
    private Long moduleId;
    private Long resourceId;
    private Long actionId;
}
