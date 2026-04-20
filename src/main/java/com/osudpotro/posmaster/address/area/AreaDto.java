package com.osudpotro.posmaster.address.area;

import lombok.Data;

@Data
public class AreaDto {
    private Long id;
    private String name;
    private AreaDto parentArea;
    private Boolean isSubArea;
    private String fullPath;
    private String parentPath;
}
