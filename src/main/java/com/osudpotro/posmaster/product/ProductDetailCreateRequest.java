package com.osudpotro.posmaster.product;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDetailCreateRequest {
//    @NotBlank(message = "Detail code is required")
//    private String productDetailCode;
//    @NotBlank(message = "Detail bar code is required")
//    private String productDetailBarCode;
//    @NotBlank(message = "Detail sku required")
//    private String productDetailSku;
    @NotNull(message = "Detail regular price is required")
    private Double regularPrice;
//    @NotNull(message = "Detail Old price is required")
    private Double oldPrice;
    private Double purchasePrice;
    @NotNull(message = "Detail size is required")
    private Long sizeId;
//    @NotNull(message = "Detail color is required")
    private Long colorId;
    private int bulkSize;
    private Long parentProductDetailId;
    private Boolean isPurchaseUnit;
}
