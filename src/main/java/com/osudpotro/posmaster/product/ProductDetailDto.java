package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.variantunit.VariantUnitDto;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;

@Data
public class ProductDetailDto {
    private Long id;
    private String productDetailCode;
    private String productDetailBarCode;
    private byte[] productDetailBarCodeImage;
    private String productDetailSku;
    private Double regularPrice;//sell price
    private Double oldPrice;//sell price
    @Column(nullable = true)
    private Double purchasePrice;
    private VariantUnitDto size;
    private VariantUnitDto color;
    private int bulkSize;
    private int atomQty;
    private VariantUnitDto parentSize;
    private List<Long> productDetailIds;
    private ProductDetailDto parentProductDetail;
    private ProductDto product;
}
