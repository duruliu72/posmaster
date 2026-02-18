package com.osudpotro.posmaster.user;

import com.osudpotro.posmaster.action.ActionDto;
import com.osudpotro.posmaster.resource.ResourceDto;
import com.osudpotro.posmaster.role.RoleDto;
import com.osudpotro.posmaster.security.PermissionDetailDto;
import com.osudpotro.posmaster.security.PermissionDto;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CustomUserMapper {
    //Mapping Here
    //Entity → DTO
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUserName(user.getUserName());
        userDto.setEmail(user.getEmail());
        userDto.setMobile(user.getMobile());
        userDto.setCreatedAt(user.getCreatedAt());
        Set<RoleDto> roles = new HashSet<>();
        if (user.getRoles() != null) {
            user.getRoles().forEach(role -> {
                RoleDto roleDto = new RoleDto();
                roleDto.setId(role.getId());
                roleDto.setName(role.getName());
                roleDto.setRoleKey(role.getRoleKey());
//                ResourceRequest Add
                Set<PermissionDto> permissions = new HashSet<>();
                role.getPermissions().forEach(permission -> {
                    PermissionDto permissionDto = new PermissionDto();
                    ResourceDto resourceDto = new ResourceDto();
                    resourceDto.setId(permission.getResource().getId());
                    resourceDto.setName(permission.getResource().getName());
                    resourceDto.setResourceKey(permission.getResource().getResourceKey());
                    resourceDto.setUrl(permission.getResource().getUrl());
                    permissionDto.setResource(resourceDto);
                    permissionDto.setPermissionType(permission.getPermissionType());
                    Set<PermissionDetailDto> permissionDetails = new HashSet<>();
                    permission.getPermissionDetails().forEach(pd -> {
                        PermissionDetailDto permissionDetailDto = new PermissionDetailDto();
                        ActionDto actionDto = new ActionDto();
                        actionDto.setId(pd.getAction().getId());
                        actionDto.setName(pd.getAction().getName());
                        permissionDetailDto.setAction(actionDto);
                        permissionDetails.add(permissionDetailDto);
                    });
                    permissionDto.setPermissionDetails(permissionDetails);
                    permissions.add(permissionDto);
                });
                roleDto.setPermissions(permissions);
                roles.add(roleDto);
            });
        }
        userDto.setRoles(roles);
        Set<PermissionDto> permissions = new HashSet<>();
        if (user.getPermissions() != null) {
            PermissionDto permissionDto = new PermissionDto();
            user.getPermissions().forEach(permission -> {
                ResourceDto resourceDto = new ResourceDto();
                resourceDto.setId(permission.getResource().getId());
                resourceDto.setName(permission.getResource().getName());
                resourceDto.setResourceKey(permission.getResource().getResourceKey());
                resourceDto.setUrl(permission.getResource().getUrl());
                permissionDto.setResource(resourceDto);
                permissionDto.setPermissionType(permission.getPermissionType());
                Set<PermissionDetailDto> permissionDetails = new HashSet<>();
                permission.getPermissionDetails().forEach(pd -> {
                    PermissionDetailDto permissionDetailDto = new PermissionDetailDto();
                    ActionDto actionDto = new ActionDto();
                    actionDto.setId(pd.getAction().getId());
                    actionDto.setName(pd.getAction().getName());
                    permissionDetailDto.setAction(actionDto);
                    permissionDetails.add(permissionDetailDto);
                });
                permissionDto.setPermissionDetails(permissionDetails);
                permissions.add(permissionDto);
            });
        }
        userDto.setPermissions(permissions);
        return userDto;
    }

    public UserMainDto toMainDto(User user) {
        UserMainDto userMainDto = new UserMainDto();
        userMainDto.setId(user.getId());
        if (user.getAdminUser() != null) {
            userMainDto.setUserName(user.getUserName());
            userMainDto.setEmail(user.getEmail());
        }
        userMainDto.setCreatedAt(user.getCreatedAt());
        return userMainDto;
    }
}


