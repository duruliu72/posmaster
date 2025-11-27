package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.variantunit.VariantUnitDto;
import lombok.Data;

@Data
public class ProductDetailDto {
    private Long id;
    private String productChildCode;
    private String productChildBarCode;
    private String productChildSku;
    private double regularPrice;
    private VariantUnitDto size;
    private VariantUnitDto color;
    private int bulkSize;
    private int atomQty;
    private VariantUnitDto parentSize;
}
