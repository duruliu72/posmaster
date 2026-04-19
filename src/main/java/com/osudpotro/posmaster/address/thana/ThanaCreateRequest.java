package com.osudpotro.posmaster.address.thana;

import lombok.Data;

@Data
public class ThanaCreateRequest {
    private String name;
    private Long districtId;
    private Long divisionId;
}
