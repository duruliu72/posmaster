package com.osudpotro.posmaster.resource.ui;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UiResourceCreateRequest {
    private String name;
    private String uiResourceKey;
    private String pageUrl;
    private String icon;
    private Long parentId;
    private Integer orderNo;
    private Boolean isSideLoc;
    private List<UiResourceActionRequest> actionWithChecks=new ArrayList<>();
}
