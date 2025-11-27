package com.osudpotro.posmaster.variantunit;

import com.osudpotro.posmaster.varianttype.VariantType;
import com.osudpotro.posmaster.varianttype.VariantTypeDto;
import lombok.Data;

@Data
public class VariantUnitDto {
    private Long id;
    private String name;
    private VariantTypeDto variantType;
}