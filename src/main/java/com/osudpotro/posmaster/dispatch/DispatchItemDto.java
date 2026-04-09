package com.osudpotro.posmaster.dispatch;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DispatchItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productCode;
    private String productBarCode;
    private Long productTypeId;
    private String productTypeName;
    private Long manufacturerId;
    private String manufacturerName;
    private Long productDetailId;
    private String productDetailCode;
    private String productDetailBarCode;
    private String productDetailSku;
    private Long sizeId;
    private String sizeName;
    private BigDecimal purchasePrice;
    private Integer dispatchQty;
    private Integer requestedQty;
    private Integer updatedQty;
}
