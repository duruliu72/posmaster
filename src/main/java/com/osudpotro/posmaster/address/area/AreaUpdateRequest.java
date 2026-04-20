package com.osudpotro.posmaster.address.area;

import lombok.Data;

@Data
public class AreaUpdateRequest {
    private String name;
    private Long parentAreaId;
    private Boolean isSubArea;
}
