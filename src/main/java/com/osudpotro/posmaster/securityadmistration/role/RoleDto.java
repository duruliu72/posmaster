package com.osudpotro.posmaster.securityadmistration.role;

import com.osudpotro.posmaster.securityadmistration.permission.PermissionDto;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RoleDto {
    private Long id;
    private String name;
    private String roleKey;
    private Set<PermissionDto> permissions = new HashSet<>();
}
