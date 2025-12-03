package com.osudpotro.posmaster.resource;

import lombok.Data;

@Data
public class UiResourceUpdateRequest {
    private String name;
    private String uiResourceKey;
    private String pageUrl;
    private String icon;
    private Long parentId;
    private Integer orderNo;
    private Boolean isSideLoc;
}
