package com.osudpotro.posmaster.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.osudpotro.posmaster.role.RoleDto;
import com.osudpotro.posmaster.security.PermissionDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private Set<RoleDto> roles;
    private Set<PermissionDto> permissions = new HashSet<>();
}