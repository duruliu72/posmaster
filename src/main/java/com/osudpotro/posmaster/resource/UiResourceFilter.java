package com.osudpotro.posmaster.resource;

import lombok.Data;

@Data
public class UiResourceFilter {
    private String name;
    private String uiResourceKey;
    private String pageUrl;
    private Integer status;
}
