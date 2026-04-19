package com.osudpotro.posmaster.address.district;

import lombok.Data;

@Data
public class DistrictCreateRequest {
    private String name;
    private Long divisionId;
}
