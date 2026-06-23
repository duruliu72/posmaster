package com.osudpotro.posmaster.securityadmistration.permission;

import com.osudpotro.posmaster.category.Category;
import com.osudpotro.posmaster.securityadmistration.resource.Resource;
import com.osudpotro.posmaster.securityadmistration.resource.ResourceRepository;
import com.osudpotro.posmaster.securityadmistration.role.Role;
import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    @Autowired
    private AuthService authService;
    @Autowired
    private PermissionRepository permissionRep;
    @Autowired
    private ResourceRepository resourceRep;
    @Autowired
    private PermissionMapper permissionMapper;

    //   This is for Dynamic menu
    public List<PermissionResourceDto> getAllPermissionResources() {
        var authUser = authService.getCurrentUser();
        Set<Role> roles = authUser.getRoles();
        var permissions = permissionRep.findByRoleInOrUser(roles, authUser);
        var filterPermissions= permissions.stream()
                .filter(i ->
                        !i.getActiveActions().isEmpty()
                ).toList();
        List<PermissionResourceDto> resources = new ArrayList<>();
        for (var permission : filterPermissions) {
            List<PermissionResourceDto> newResources = new ArrayList<>();
            resources.addAll(getIds(permission.getResource(), newResources));
        }
        Set<PermissionResourceDto> topResources = resources.stream()
                .filter(i ->
                        i.getParentId() == null
                )
                .collect(Collectors.toSet());
        for (var topResource : topResources) {
            loadChildrenRecursively(topResource, resources);
        }
        return topResources.stream().toList();
    }

    private List<PermissionResourceDto> getIds(Resource resource, List<PermissionResourceDto> resources) {
        resources.add(permissionMapper.toPermissionResourceDto(resource));
        if (resource.getParentResource() != null) {
            getIds(resource.getParentResource(), resources);
        }
        return resources;
    }

    private void loadChildrenRecursively(PermissionResourceDto prDto, List<PermissionResourceDto> resources) {
        List<PermissionResourceDto> childResources = resources.stream()
                .filter(i ->
                        Objects.equals(i.getParentId(), prDto.getId())
                )
                .toList();
        prDto.setChildResource(childResources);
    }
}
