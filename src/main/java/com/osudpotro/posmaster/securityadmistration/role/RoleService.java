package com.osudpotro.posmaster.securityadmistration.role;

import com.osudpotro.posmaster.action.Action;
import com.osudpotro.posmaster.action.ActionNotFoundException;
import com.osudpotro.posmaster.action.ActionRepository;
import com.osudpotro.posmaster.common.EntityNotFoundException;
import com.osudpotro.posmaster.securityadmistration.module.ModuleInfo;
import com.osudpotro.posmaster.securityadmistration.module.ModuleInfoRepository;
import com.osudpotro.posmaster.securityadmistration.permission.*;
import com.osudpotro.posmaster.securityadmistration.resource.Resource;
import com.osudpotro.posmaster.securityadmistration.resource.ResourceRepository;
import com.osudpotro.posmaster.user.auth.AuthService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class RoleService {
    private final AuthService authService;
    private final RoleRepository roleRepository;
    private final ResourceRepository resourceRepository;
    private final ModuleInfoRepository moduleInfoRepo;
    private final ActionRepository actionRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepo;
    private final PermissionActionRepository paRepo;

    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toDto)
                .toList();
    }

    public Page<RoleDto> getAllEntities(RoleFilter filter, Pageable pageable) {
        return roleRepository.findAll(RoleSpecification.filter(filter), pageable).map(roleMapper::toDto);
    }

    public RoleDto createRole(RoleCreateRequest request) {
        if (roleRepository.existsByRoleKey(request.getRoleKey())) {
            throw new DuplicateRoleException();
        }
        var user = authService.getCurrentUser();
        var role = roleMapper.toEntity(request);
        role.setCreatedBy(user);
        roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    public RoleDto updateRole(Long roleId, RoleUpdateRequest request) {
        var role = roleRepository.findById(roleId).orElseThrow(EntityNotFoundException::new);
        var authUser = authService.getCurrentUser();
        role.setUpdatedBy(authUser);
        role.setName(request.getName());
        roleRepository.save(role);
        return roleMapper.toDto(role);
    }
    @Transactional
    public RoleProjectionDto updatePermissionAction(Long roleId, PermissionActionRequest request) {
        var authUser = authService.getCurrentUser();
        Role role = roleRepository.findById(roleId).orElseThrow(EntityNotFoundException::new);
        Permission permission=permissionRepo.findPermissionByRoleAndModuleAndResource(roleId,request.getModuleId(), request.getResourceId()).orElse(null);
        if (permission == null) {
            permission = new Permission();
            permission.setRole(role);
            Resource resource = resourceRepository.findById(request.getResourceId()).orElse(null);
            if(resource==null){
                throw new EntityNotFoundException("Resource not found with id "+request.getResourceId());
            }
            ModuleInfo moduleInfo = moduleInfoRepo.findById(request.getModuleId()).orElse(null);
            if(moduleInfo==null){
                throw new EntityNotFoundException("Module not found with id "+request.getModuleId());
            }
            permission.setModuleInfo(moduleInfo);
            permission.setResource(resource);
            permission.setPermissionType(PermissionType.ROLE);
            permission.setEnable(true);
            permission.setResourceChecked(true);
            permission.setCreatedBy(authUser);
            permissionRepo.save(permission);
            role.getPermissions().add(permission);
        }else {
            Resource resource = resourceRepository.findById(request.getResourceId()).orElse(null);
            if(resource==null){
                throw new EntityNotFoundException("Resource not found with id "+request.getResourceId());
            }
            ModuleInfo moduleInfo = moduleInfoRepo.findById(request.getModuleId()).orElse(null);
            if(moduleInfo==null){
                throw new EntityNotFoundException("Module not found with id "+request.getModuleId());
            }
            permission.setModuleInfo(moduleInfo);
            permission.setResource(resource);
            permissionRepo.save(permission);
        }
        PermissionAction permissionAction = paRepo.findByPermissionIdAndActionId(permission.getId(), request.getActionId()).orElse(null);
        if (permissionAction == null) {
            Action action = actionRepository.findById(request.getActionId())
                    .orElseThrow(() -> new ActionNotFoundException("Action not found"));
            permissionAction = new PermissionAction();
            permissionAction.setPermission(permission);
            permissionAction.setAction(action);
            permissionAction.setActive(true);
            permissionAction.setCreatedBy(authUser);
            permission.getPermissionActions().add(permissionAction);
        } else {
            permissionAction.setActive(!permissionAction.isActive());
            permissionAction.setUpdatedBy(authUser);
        }
        paRepo.save(permissionAction);
        return getRoleWithPermissionAction(roleId,null);
    }

    public RoleDto getRole(Long roleId) {
        var role = roleRepository.findById(roleId).orElseThrow(RoleNotFoundException::new);
        return roleMapper.toDto(role);
    }

    public RoleProjectionDto getRoleWithPermissionAction(Long roleId, Long moduleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(RoleNotFoundException::new);
        RoleProjectionDto roleDto = new RoleProjectionDto();
        roleDto.setId(role.getId());
        roleDto.setName(role.getName());
        roleDto.setRoleKey(role.getRoleKey());
        var permissions = permissionRepo.getRoleResources(roleId, moduleId);
        List<PermissionProjectionDto> permissionList = new ArrayList<>();
        for (var permission : permissions) {
            PermissionProjectionDto pDto = new PermissionProjectionDto();
            pDto.setModuleId(permission.getModuleId());
            pDto.setModuleName(permission.getModuleName());
            pDto.setResourceId(permission.getResourceId());
            pDto.setResourceName(permission.getResourceName());
            pDto.setPermissionId(permission.getPermissionId());
            var permissionActionList = paRepo.getPermissionActions(roleId, permission.getResourceId());
            List<PermissionActionProjectionDto> permissionActions = new ArrayList<>();
            for (var pAction : permissionActionList) {
                PermissionActionProjectionDto paDto = new PermissionActionProjectionDto();
                paDto.setResourceId(pAction.getResourceId());
                paDto.setActionId(pAction.getActionId());
                paDto.setActionName(pAction.getActionName());
                paDto.setIsActive(pAction.getIsActive());
                permissionActions.add(paDto);
            }
            pDto.setPermissionActions(permissionActions);
            permissionList.add(pDto);
        }
        roleDto.setPermissions(permissionList);
        return roleDto;
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
