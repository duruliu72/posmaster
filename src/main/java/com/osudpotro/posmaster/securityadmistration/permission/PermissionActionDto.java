package com.osudpotro.posmaster.securityadmistration.permission;

import com.osudpotro.posmaster.action.ActionDto;
import lombok.Data;

@Data
public class PermissionActionDto {
    private ActionDto action;
    private boolean isActive;
}
