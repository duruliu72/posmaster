package com.osudpotro.posmaster.resource;
import com.osudpotro.posmaster.action.ActionDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResourceMapper {
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
        if(resource.getParentResource()!=null){
            Resource child=resource.getParentResource();
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
//        For UiResource Actions
        List<ResourceActionDto> resourceActions = new ArrayList<>();
        for (ResourceAction resourceAction : resource.getResourceActions()) {
            ResourceActionDto resourceActionDto=new ResourceActionDto();
            ActionDto actionDto=new ActionDto();
            actionDto.setId(resourceAction.getAction().getId());
            actionDto.setName(resourceAction.getAction().getName());
            resourceActionDto.setAction(actionDto);
            resourceActionDto.setChecked(resourceAction.getChecked());
            resourceActions.add(resourceActionDto);
        }
        resourceDto.setResourceActions(resourceActions);
        return resourceDto;
    }

    public Resource toEntity(ResourceCreateRequest request) {
        Resource resource = new Resource();
        resource.setName(request.getName());
        resource.setResourceKey(request.getResourceKey());
        resource.setUrl(request.getUrl());
        resource.setIcon(request.getIcon());
        if (request.getIsSideLoc() != null) {
            resource.setSideLoc(request.getIsSideLoc());
        }
        resource.setOrderNo(request.getOrderNo());
        return resource;
    }

    public void update(ResourceUpdateRequest request, Resource resource) {
        resource.setName(request.getName());
        resource.setResourceKey(request.getResourceKey());
        resource.setUrl(request.getUrl());
        resource.setIcon(request.getIcon());
        if (request.getIsSideLoc() != null) {
            resource.setSideLoc(request.getIsSideLoc());
        }
        resource.setOrderNo(request.getOrderNo());
    }
}
