package com.osudpotro.posmaster.securityadmistration.role;

import lombok.Data;

@Data
public class PermissionActionRequest {
//    private Long roleId;
    private Long moduleId;
    private Long resourceId;
    private Long actionId;
}
