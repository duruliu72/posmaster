package com.osudpotro.posmaster.variantunit;

import lombok.Data;

@Data
public class VariantUnitCreateRequest {
    private String name;
    private Long variantTypeId;
}