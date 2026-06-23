package com.osudpotro.posmaster.securityadmistration.permission;

import lombok.Data;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private List<PermissionResourceDto> childResource;
    private List<PermissionActionDto> permissionActions=new ArrayList<>();
    private Set<PermissionAction> activePermissionActions = new HashSet<>();
}
