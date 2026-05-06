package com.osudpotro.posmaster.address.upozila;

import lombok.Data;

@Data
public class UpozilaUpdateRequest {
    private String name;
    private Long districtId;
    private Long divisionId;
}
