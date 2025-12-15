package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.variantunit.VariantUnitDto;
import lombok.Data;

import java.util.List;

@Data
public class ProductDetailDto {
    private Long id;
    private String productDetailCode;
    private String productDetailBarCode;
    private String productDetailSku;
    private double regularPrice;
    private VariantUnitDto size;
    private VariantUnitDto color;
    private int bulkSize;
    private int atomQty;
    private VariantUnitDto parentSize;
    private List<Long> productDetailIds;
    private ProductDetailDto parentProductDetail;
}
