package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.generic.GenericDto;
import com.osudpotro.posmaster.genericunit.GenericUnitDto;
import lombok.Data;

@Data
public class ProductGenericDto {
    private GenericDto generic;
    private Double dose;
    private GenericUnitDto genericUnit;
}
