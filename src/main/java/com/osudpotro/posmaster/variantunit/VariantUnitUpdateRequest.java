package com.osudpotro.posmaster.variantunit;

import lombok.Data;

@Data
public class VariantUnitUpdateRequest {
    private String name;
    private Long variantTypeId;
}