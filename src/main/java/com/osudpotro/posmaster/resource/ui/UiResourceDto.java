package com.osudpotro.posmaster.resource.ui;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UiResourceDto {
    private Long id;
    private String name;
    private String uiResourceKey;
    private String pageUrl;
    private String icon;
    private Integer orderNo;
    private Boolean isSideLoc;
    private UiResourceDto parentUiResource;
    private List<UiResourceActionDto> uiResourceActions=new ArrayList<>();
}
