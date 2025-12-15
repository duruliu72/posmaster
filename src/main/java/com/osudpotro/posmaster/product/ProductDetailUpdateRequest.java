package com.osudpotro.posmaster.product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDetailUpdateRequest {
    @NotBlank(message = "Child code is required")
    private String productDetailCode;
    @NotBlank(message = "Detail bar code is required")
    private String productDetailBarCode;
    @NotBlank(message = "Detail sku required")
    private String productDetailSku;
    @NotNull(message = "Detail regular price is required")
    private double regularPrice;
    private double oldPrice;
    @NotNull(message = "Detail size is required")
    private Long sizeId;
    @NotNull(message = "Detail color is required")
    private Long colorId;
    private int bulkSize;
    private Long parent_size_id;
}
