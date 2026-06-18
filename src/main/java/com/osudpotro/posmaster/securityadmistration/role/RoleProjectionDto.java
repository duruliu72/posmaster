package com.osudpotro.posmaster.securityadmistration.role;

import com.osudpotro.posmaster.securityadmistration.permission.PermissionProjectionDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class RoleProjectionDto {
    private Long id;
    private String name;
    private String roleKey;
    private List<PermissionProjectionDto> permissions = new ArrayList<>();
}
