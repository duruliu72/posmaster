package com.osudpotro.posmaster.securityadmistration.resource;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResourceDto {
    private Long id;
    private String name;
    private String resourceKey;
    private String url;
    private String icon;
    private Integer orderNo;
    private Boolean isSideLoc;
    private Long moduleId;
    private String moduleName;
    private ResourceDto parentResource;
    private List<ResourceActionDto> resourceActions=new ArrayList<>();
}
