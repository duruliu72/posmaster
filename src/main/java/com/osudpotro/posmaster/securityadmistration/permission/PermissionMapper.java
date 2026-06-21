package com.osudpotro.posmaster.securityadmistration.permission;

import com.osudpotro.posmaster.action.ActionDto;
import com.osudpotro.posmaster.securityadmistration.module.ModuleInfo;
import com.osudpotro.posmaster.securityadmistration.resource.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PermissionMapper {
    public ResourceDto toDto(Resource resource) {
        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setId(resource.getId());
        resourceDto.setName(resource.getName());
        resourceDto.setResourceKey(resource.getResourceKey());
        resourceDto.setUrl(resource.getUrl());
        resourceDto.setIcon(resource.getIcon());
        resourceDto.setIsSideLoc(resource.isSideLoc());
        resourceDto.setOrderNo(resource.getOrderNo());
//        For Parent
        if (resource.getParentResource() != null) {
            Resource child = resource.getParentResource();
            ResourceDto parentResourceDto = new ResourceDto();
            parentResourceDto.setId(child.getId());
            parentResourceDto.setName(child.getName());
            parentResourceDto.setResourceKey(child.getResourceKey());
            parentResourceDto.setUrl(child.getUrl());
            parentResourceDto.setIcon(child.getIcon());
            parentResourceDto.setIsSideLoc(child.isSideLoc());
            resourceDto.setOrderNo(child.getOrderNo());
            resourceDto.setParentResource(parentResourceDto);
        }
//        For Resource Actions
        List<ResourceActionDto> resourceActions = new ArrayList<>();
        for (ResourceAction resourceAction : resource.getResourceActions()) {
            ResourceActionDto resourceActionDto = new ResourceActionDto();
            ActionDto actionDto = new ActionDto();
            actionDto.setId(resourceAction.getAction().getId());
            actionDto.setName(resourceAction.getAction().getName());
            resourceActionDto.setAction(actionDto);
            resourceActionDto.setChecked(resourceAction.getChecked());
            resourceActions.add(resourceActionDto);
        }
        resourceDto.setResourceActions(resourceActions);
        if (resource.getModuleInfo() != null) {
            ModuleInfo moduleInfo = resource.getModuleInfo();
            resourceDto.setModuleId(moduleInfo.getId());
            resourceDto.setModuleName(moduleInfo.getModuleName());
        }

        return resourceDto;
    }

    public PermissionResourceDto toPermissionResourceDto(Permission permission) {
        PermissionResourceDto permissionResourceDto = new PermissionResourceDto();
        Resource resource = permission.getResource();
        permissionResourceDto.setId(resource.getId());
        permissionResourceDto.setName(resource.getName());
        permissionResourceDto.setResourceKey(resource.getResourceKey());
        permissionResourceDto.setUrl(resource.getUrl());
        permissionResourceDto.setIcon(resource.getIcon());
        permissionResourceDto.setIsSideLoc(resource.isSideLoc());
        permissionResourceDto.setOrderNo(resource.getOrderNo());
        if(resource.getParentResource()!=null){
            permissionResourceDto.setParentId(resource.getParentResource().getId());
        }
        return permissionResourceDto;
    }

    public PermissionResourceDto toPermissionResourceDto(Resource resource) {
        PermissionResourceDto permissionResourceDto = new PermissionResourceDto();
        permissionResourceDto.setId(resource.getId());
        permissionResourceDto.setName(resource.getName());
        permissionResourceDto.setResourceKey(resource.getResourceKey());
        permissionResourceDto.setUrl(resource.getUrl());
        permissionResourceDto.setIcon(resource.getIcon());
        permissionResourceDto.setIsSideLoc(resource.isSideLoc());
        permissionResourceDto.setOrderNo(resource.getOrderNo());
        if(resource.getParentResource()!=null){
            permissionResourceDto.setParentId(resource.getParentResource().getId());
        }
        return permissionResourceDto;

    }

}
