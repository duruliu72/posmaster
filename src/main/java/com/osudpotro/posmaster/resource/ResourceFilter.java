package com.osudpotro.posmaster.resource;

import lombok.Data;

@Data
public class ResourceFilter {
    private String name;
    private String resourceKey;
    private String url;
    private Integer status;
}
