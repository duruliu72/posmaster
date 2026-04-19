package com.osudpotro.posmaster.address.area;

import lombok.Data;

@Data
public class AreaCreateRequest {
    private String name;
    private Long districtId;
    private Long divisionId;
}
