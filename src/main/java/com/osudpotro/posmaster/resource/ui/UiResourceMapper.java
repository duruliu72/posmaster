package com.osudpotro.posmaster.resource.ui;
import com.osudpotro.posmaster.action.ActionDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UiResourceMapper {
    public UiResourceDto toDto(UiResource uiResource) {
        UiResourceDto uiResourceDto = new UiResourceDto();
        uiResourceDto.setId(uiResource.getId());
        uiResourceDto.setName(uiResource.getName());
        uiResourceDto.setUiResourceKey(uiResource.getUiResourceKey());
        uiResourceDto.setPageUrl(uiResource.getPageUrl());
        uiResourceDto.setIcon(uiResource.getIcon());
        uiResourceDto.setIsSideLoc(uiResource.isSideLoc());
        uiResourceDto.setOrderNo(uiResource.getOrderNo());
//        For Parent
        if(uiResource.getParentUiResource()!=null){
            UiResource child=uiResource.getParentUiResource();
            UiResourceDto parentUiResourceDto = new UiResourceDto();
            parentUiResourceDto.setId(child.getId());
            parentUiResourceDto.setName(child.getName());
            parentUiResourceDto.setUiResourceKey(child.getUiResourceKey());
            parentUiResourceDto.setPageUrl(child.getPageUrl());
            parentUiResourceDto.setIcon(child.getIcon());
            parentUiResourceDto.setIsSideLoc(child.isSideLoc());
            uiResourceDto.setOrderNo(child.getOrderNo());
            uiResourceDto.setParentUiResource(parentUiResourceDto);
        }
//        For UiResource Actions
        List<UiResourceActionDto> uiResourceActions = new ArrayList<>();
        for (UiResourceAction uiResourceAction : uiResource.getUiResourceActions()) {
            UiResourceActionDto uiResourceActionDto=new UiResourceActionDto();
            ActionDto actionDto=new ActionDto();
            actionDto.setId(uiResourceAction.getAction().getId());
            actionDto.setName(uiResourceAction.getAction().getName());
            uiResourceActionDto.setAction(actionDto);
            uiResourceActionDto.setChecked(uiResourceAction.getChecked());
            uiResourceActions.add(uiResourceActionDto);
        }
        uiResourceDto.setUiResourceActions(uiResourceActions);
        return uiResourceDto;
    }

    public UiResource toEntity(UiResourceCreateRequest request) {
        UiResource uiResource = new UiResource();
        uiResource.setName(request.getName());
        uiResource.setUiResourceKey(request.getUiResourceKey());
        uiResource.setPageUrl(request.getPageUrl());
        uiResource.setIcon(request.getIcon());
        if (request.getIsSideLoc() != null) {
            uiResource.setSideLoc(request.getIsSideLoc());
        }
        uiResource.setOrderNo(request.getOrderNo());
        return uiResource;
    }

    public void update(UiResourceUpdateRequest request, UiResource uiResource) {
        uiResource.setName(request.getName());
        uiResource.setUiResourceKey(request.getUiResourceKey());
        uiResource.setPageUrl(request.getPageUrl());
        uiResource.setIcon(request.getIcon());
        if (request.getIsSideLoc() != null) {
            uiResource.setSideLoc(request.getIsSideLoc());
        }
        uiResource.setOrderNo(request.getOrderNo());
    }
}
