package com.osudpotro.posmaster.security;

import com.osudpotro.posmaster.action.ActionDto;
import com.osudpotro.posmaster.resource.ApiResourceDto;
import lombok.Data;

@Data
public class PermissionDetailDto {
    private ActionDto action;
}
