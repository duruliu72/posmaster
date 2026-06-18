package com.osudpotro.posmaster.securityadmistration.permission;


import lombok.Data;

@Data
public class PermissionActionProjectionDto {
   private Long resourceId;
    private Long actionId;
    private Boolean isActive;
    private String actionName;
}
