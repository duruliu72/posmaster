package com.osudpotro.posmaster.securityadmistration.permission;

import com.osudpotro.posmaster.securityadmistration.resource.Resource;
import com.osudpotro.posmaster.securityadmistration.resource.ResourceRepository;
import com.osudpotro.posmaster.securityadmistration.role.Role;
import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        var permissionResources = new ArrayList<>(
                permissions.stream()
                        .map(permissionMapper::toPermissionResourceDto)
                        .toList()
        );
        List<Long> parentIds = permissions.stream()
                .filter(i ->
                        i.getResource() != null && i.getResource().getParentResource() != null
                )
                .map(p -> p.getResource().getParentResource().getId())
                .distinct()
                .toList();
        List<Resource> parentResourceList = resourceRep.findResourceByIds(parentIds);
        List<PermissionResourceDto> list=new ArrayList<>();
        for (var item : parentResourceList) {
            List<PermissionResourceDto> subList=new ArrayList<>();
            var parentResources = getResource(item, subList);
            list.addAll(parentResources);
        }
        permissionResources.addAll(list);
        return permissionResources;
    }

    private List<PermissionResourceDto> getResource(Resource resource, List<PermissionResourceDto> list) {
        list.add(permissionMapper.toPermissionResourceDto(resource));
        if (resource.getParentResource() != null) {
            getResource(resource.getParentResource(), list);
        }
        return list;
    }
}
