package com.osudpotro.posmaster.resource;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResourceCreateRequest {
    private String name;
    private String resourceKey;
    private String url;
    private String icon;
    private Long parentId;
    private Integer orderNo;
    private Boolean isSideLoc;
    private List<ResourceActionRequest> actionWithChecks=new ArrayList<>();
}
