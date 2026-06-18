package com.osudpotro.posmaster.securityadmistration.permission;

public interface PermissionActionProjection {
    Long getResourceId();
    Long getActionId();
    Boolean getChecked();
    Boolean getIsActive();
    String getActionName();
}
