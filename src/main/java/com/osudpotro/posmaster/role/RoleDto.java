package com.osudpotro.posmaster.role;

import com.osudpotro.posmaster.security.PermissionDto;
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
