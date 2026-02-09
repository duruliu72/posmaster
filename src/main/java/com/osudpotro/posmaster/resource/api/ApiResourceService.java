package com.osudpotro.posmaster.resource.api;


import com.osudpotro.posmaster.action.Action;
import com.osudpotro.posmaster.action.ActionNotFoundException;
import com.osudpotro.posmaster.action.ActionRepository;
import com.osudpotro.posmaster.auth.AuthService;
import com.osudpotro.posmaster.role.Role;
import com.osudpotro.posmaster.role.RoleRepository;
import com.osudpotro.posmaster.security.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@AllArgsConstructor
@Service
public class ApiResourceService {
    private final AuthService authService;
    private final ApiResourceRepository apiResourceRepository;
    private final ApiResourceMapper apiResourceMapper;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PermissionDetailRepository permissionDetailRepository;
    private final ActionRepository actionRepository;

    public List<ApiResourceDto> getAllApiResources() {
        return apiResourceRepository.findAll()
                .stream()
                .map(apiResourceMapper::toDto)
                .toList();
    }

    public ApiResourceDto createApiResource(ApiResourceCreateRequest request) {
        if (apiResourceRepository.existsByName(request.getName())) {
            throw new DuplicateApiResourceException();
        }
        var user = authService.getCurrentUser();
        var apiResource = apiResourceMapper.toEntity(request);
        apiResource.setApiResourceKey(request.getName());
        apiResource.setApiResourceKey(request.getApiResourceKey());
        apiResource.setApiUrl(request.getApiUrl());
        apiResource.setCreatedBy(user);
        apiResourceRepository.save(apiResource);
        Role findSuperAdminRole = roleRepository.findByRoleKey("ROLE_SUPER_ADMIN")
                .orElseGet(() -> {
                    Role superAdmin = new Role();
                    superAdmin.setName("Super AdminUser");
                    superAdmin.setRoleKey("ROLE_SUPER_ADMIN");
                    superAdmin.setCreatedBy(user);
                    superAdmin.setUsers(new HashSet<>());
                    superAdmin.setPermissions(new HashSet<>());
                    return roleRepository.save(superAdmin);
                });
        // === PERMISSIONS ===
        Permission permission = new Permission();
        permission.setRole(findSuperAdminRole);
        permission.setApiResource(apiResource);
        permission.setPermissionType(PermissionType.ROLE);
        permission.setEnable(true);
        permission.setCreatedBy(user);
        permission = permissionRepository.save(permission);
        //=== ResourceRequest Details====
        Action view = actionRepository.findByName("READ").orElseThrow(()->new ActionNotFoundException());
        Action create = actionRepository.findByName("CREATE").orElseThrow(()->new ActionNotFoundException());
        PermissionDetail detail1 = new PermissionDetail();
        detail1.setPermission(permission);
        detail1.setAction(view);
        detail1.setCreatedBy(user);
        PermissionDetail detail2 = new PermissionDetail();
        detail2.setPermission(permission);
        detail2.setAction(create);
        detail2.setCreatedBy(user);
        permissionDetailRepository.saveAll(Arrays.asList(detail1, detail2));
        return apiResourceMapper.toDto(apiResource);
    }

    public ApiResourceDto updateApiResource(Long resourceId, ApiResourceUpdateRequest request) {
        var resource = apiResourceRepository.findById(resourceId).orElseThrow(ApiResourceNotFoundException::new);
        var user = authService.getCurrentUser();
        apiResourceMapper.update(request, resource);
        resource.setUpdatedBy(user);
        apiResourceRepository.save(resource);
        return apiResourceMapper.toDto(resource);
    }

    public ApiResourceDto getApiResource(Long resourceId) {
        var resource = apiResourceRepository.findById(resourceId).orElseThrow(() -> new ApiResourceNotFoundException("Api Resource not found with ID: " + resourceId));
        return apiResourceMapper.toDto(resource);
    }

    public ApiResourceDto activateApiResource(Long resourceId) {
        var resource = apiResourceRepository.findById(resourceId).orElseThrow(() -> new ApiResourceNotFoundException("Api Resource not found with ID: " + resourceId));
        var user = authService.getCurrentUser();
        resource.setStatus(1);
        resource.setUpdatedBy(user);
        apiResourceRepository.save(resource);
        return apiResourceMapper.toDto(resource);
    }

    public ApiResourceDto deactivateApiResource(Long resourceId) {
        var resource = apiResourceRepository.findById(resourceId).orElseThrow(() -> new ApiResourceNotFoundException("Api Resource not found with ID: " + resourceId));
        var user = authService.getCurrentUser();
        resource.setStatus(2);
        resource.setUpdatedBy(user);
        apiResourceRepository.save(resource);
        return apiResourceMapper.toDto(resource);
    }

    public ApiResourceDto deleteApiResource(Long resourceId) {
        var resource = apiResourceRepository.findById(resourceId).orElseThrow(() -> new ApiResourceNotFoundException("Api Resource not found with ID: " + resourceId));
        var user = authService.getCurrentUser();
        resource.setStatus(3);
        resource.setUpdatedBy(user);
        apiResourceRepository.save(resource);
        return apiResourceMapper.toDto(resource);
    }
}
