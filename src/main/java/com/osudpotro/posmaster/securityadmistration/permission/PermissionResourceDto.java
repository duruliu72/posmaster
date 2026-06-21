package com.osudpotro.posmaster.securityadmistration.permission;

import com.osudpotro.posmaster.securityadmistration.resource.ResourceDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PermissionResourceDto {
    private Long id;
    private String name;
    private String resourceKey;
    private String url;
    private String icon;
    private Integer orderNo;
    private Boolean isSideLoc;
    private Long moduleId;
    private String moduleName;
    private Long parentId;
    private List<ResourceDto> childResource;
    private List<PermissionActionDto> permissionActions=new ArrayList<>();
}
