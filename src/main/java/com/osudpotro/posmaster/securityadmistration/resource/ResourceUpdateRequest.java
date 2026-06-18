package com.osudpotro.posmaster.securityadmistration.resource;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResourceUpdateRequest {
    private Long moduleId;
    private String name;
    private String resourceKey;
    private String url;
    private String icon;
    private Long parentId;
    private Integer orderNo;
    private Boolean isSideLoc;
    private List<ResourceActionRequest> actionWithChecks=new ArrayList<>();
}
