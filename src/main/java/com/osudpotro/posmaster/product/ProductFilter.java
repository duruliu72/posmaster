package com.osudpotro.posmaster.product;

import lombok.Data;

@Data
public class ProductFilter {
    private String productName;
    private String productCode;
    private String productBarCode;
    private String productSku;
    private Long categoryId;
    private Long brandId;
    private Long productTypeId;
    private Long manufacturerId;
    private Integer status;
}
