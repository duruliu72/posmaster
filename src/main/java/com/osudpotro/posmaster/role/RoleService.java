package com.osudpotro.posmaster.role;

import com.osudpotro.posmaster.action.Action;
import com.osudpotro.posmaster.action.ActionNotFoundException;
import com.osudpotro.posmaster.action.ActionRepository;
import com.osudpotro.posmaster.resource.Resource;
import com.osudpotro.posmaster.resource.ResourceRepository;
import com.osudpotro.posmaster.user.auth.AuthService;
import com.osudpotro.posmaster.security.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class RoleService {
    private final AuthService authService;
    private final RoleRepository roleRepository;
    private final ResourceRepository resourceRepository;
    private final ActionRepository actionRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;
    private final PermissionDetailRepository permissionDetailRepository;

    public List<RoleDto> gerAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toDto)
                .toList();
    }

    public RoleDto createRole(RoleCreateRequest request) {
        if (roleRepository.existsByRoleKey(request.getRoleKey())) {
            throw new DuplicateRoleException();
        }
        var user = authService.getCurrentUser();
        var role = roleMapper.toEntity(request);
        role.setCreatedBy(user);
        Set<Permission> permissions = new HashSet<>();
        request.getResources().forEach(r -> {
            Permission permission = new Permission();
            Resource resource = resourceRepository.findById(r.getResourceId()).orElseThrow();
            permission.setResource(resource);
            permission.setPermissionType(PermissionType.ROLE);
            permission.setEnable(true);
            permission.setRole(role);
            permission.setResourceChecked(r.isResourceChecked());
            permission.setCreatedBy(user);
            Set<PermissionDetail> permissionDetails = new HashSet<>();
            r.getActions().forEach(a -> {
                Action action = actionRepository.findById(a.getActionId()).orElseThrow();
                PermissionDetail detail = new PermissionDetail();
                detail.setPermission(permission);
                detail.setAction(action);
                detail.setActionChecked(a.isActionChecked());
                detail.setCreatedBy(user);
                permissionDetails.add(detail);
            });
            permission.setPermissionDetails(permissionDetails);
            permissions.add(permission);
        });

        role.setPermissions(permissions);
        roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    public RoleDto updateRole(Long roleId, RoleUpdateRequest request) {
        var role = roleRepository.findById(roleId).orElseThrow(RoleNotFoundException::new);
        var user = authService.getCurrentUser();
//      Reset permission and permission details
        var resourceIds = request.getResources().stream().map(ResourceRequest::getResourceId).toList();
        permissionRepository.updatePermissionByRoleAndResources(false, resourceIds, roleId);
        var permissions = permissionRepository.findPermissionByRoleAndResources(resourceIds, roleId);
        var permissionIds = permissions.stream().map(Permission::getId).toList();
        permissionDetailRepository.updatePermissionDetailByPermissions(false, permissionIds);
//      Update Permission of a Role
        roleMapper.update(request, role);
        role.setUpdatedBy(user);
        role.setName(request.getName());
        Set<Permission> permissionList = role.getPermissions();
        for (ResourceRequest r : request.getResources()) {
            Permission findPermission = permissionList.stream()
                    .filter(p -> p.getResource().getId().equals(r.getResourceId()) && p.getRole().getId().equals(roleId))
                    .findFirst().orElse(null);

            if (findPermission == null) {
                Permission permission = new Permission();
                Resource apiResource = resourceRepository.findById(r.getResourceId()).orElseThrow();
                permission.setResource(apiResource);
                permission.setPermissionType(PermissionType.ROLE);
                permission.setEnable(true);
                permission.setResourceChecked(r.isResourceChecked());
                permission.setCreatedBy(user);
                permission.setRole(role);
                Set<PermissionDetail> permissionDetails = new HashSet<>();
                r.getActions().forEach(a -> {
                    Action action = actionRepository.findById(a.getActionId()).orElseThrow();
                    PermissionDetail detail = new PermissionDetail();
                    detail.setPermission(permission);
                    detail.setAction(action);
                    detail.setActionChecked(a.isActionChecked());
                    detail.setCreatedBy(user);
                    permissionDetails.add(detail);
                });
                permission.setPermissionDetails(permissionDetails);
                permissionList.add(permission);
            } else {
                findPermission.setUpdatedBy(user);
                findPermission.setEnable(true);
                findPermission.setResourceChecked(r.isResourceChecked());
                Set<PermissionDetail> permissionDetails = findPermission.getPermissionDetails();
                for (ActionRequest a : r.getActions()) {
                    PermissionDetail findPermissionDetail = permissionDetails.stream()
                            .filter(d -> d.getAction().getId().equals(a.getActionId()))
                            .findFirst()
                            .orElse(null);
                    if (findPermissionDetail == null) {
                        Action action = actionRepository.findById(a.getActionId())
                                .orElseThrow(() -> new ActionNotFoundException("Action not found"));
                        PermissionDetail newDetail = new PermissionDetail();
                        newDetail.setPermission(findPermission);
                        newDetail.setAction(action);
                        newDetail.setActionChecked(a.isActionChecked());
                        newDetail.setCreatedBy(user);
                        permissionDetails.add(newDetail);
                    } else {
                        findPermissionDetail.setActionChecked(a.isActionChecked());
                        findPermissionDetail.setUpdatedBy(user);
                    }
                }
            }
        }
        role.setPermissions(permissionList);
        roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    public RoleDto getRole(Long roleId) {
        var role = roleRepository.findById(roleId).orElseThrow(RoleNotFoundException::new);
        return roleMapper.toDto(role);
    }

    public Role getRoleEntity(Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(RoleNotFoundException::new);
    }

    public RoleDto activeRole(Long roleId) {
        var role = roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException("Role not found with ID: " + roleId));
        var user = authService.getCurrentUser();
        role.setStatus(1);
        role.setUpdatedBy(user);
        roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    public RoleDto deActivateRole(Long roleId) {
        var role = roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException("Role not found with ID: " + roleId));
        var user = authService.getCurrentUser();
        role.setStatus(2);
        role.setUpdatedBy(user);
        roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    public RoleDto deleteRole(Long roleId) {
        var role = roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException("Role not found with ID: " + roleId));
        var user = authService.getCurrentUser();
        role.setStatus(3);
        role.setUpdatedBy(user);
        roleRepository.save(role);
        return roleMapper.toDto(role);
    }
}
