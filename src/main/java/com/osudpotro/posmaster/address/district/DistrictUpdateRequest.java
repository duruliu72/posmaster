package com.osudpotro.posmaster.address.district;

import lombok.Data;

@Data
public class DistrictUpdateRequest {
    private String name;
    private Long divisionId;
}
