package com.osudpotro.posmaster.address.thana;

import lombok.Data;

@Data
public class ThanaUpdateRequest {
    private String name;
    private Long districtId;
    private Long divisionId;
}
