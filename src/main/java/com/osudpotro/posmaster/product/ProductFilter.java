package com.osudpotro.posmaster.product;

import lombok.Data;

import java.util.List;

@Data
public class ProductFilter {
    private String productName;
    private String productCode;
    private String productBarCode;
    private String productSku;
    private String productDetailCode;
    private String productDetailBarCode;
    private String productDetailSku;
    private Long categoryId;
    private List<Long> ChildCategoryIds;
    private Long brandId;
    private Long productTypeId;
    private Long manufacturerId;
    private Integer status;
    private Boolean searchIncludeSubCategories;
}
