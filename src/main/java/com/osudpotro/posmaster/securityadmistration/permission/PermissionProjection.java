package com.osudpotro.posmaster.securityadmistration.permission;

public interface PermissionProjection {
    Long getPermissionId();
    Long getResourceId();
    String getResourceName();
    Long getModuleId();
    String getModuleName();
}
