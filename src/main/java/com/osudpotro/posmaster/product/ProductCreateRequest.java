package com.osudpotro.posmaster.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProductCreateRequest {
    @NotBlank(message = "Product name is required")
    private String ProductName;
    @NotBlank(message = "Product Code is required")
    private String ProductCode;
    @NotBlank(message = "Product Bar Code is required")
    private String ProductBarCode;
    @NotBlank(message = "Product Sku is required")
    private String ProductSku;
    private String description;
    @NotNull(message = "Product manufacturer is required")
    private Long manufacturerId;
    @NotNull(message = "Product Brand is required")
    private Long brandId;
    @NotNull(message = "Product Type is required")
    private Long ProductTypeId;
    @NotEmpty(message = "At least one product Details is required")
    private List<@Valid ProductDetailCreateRequest> details;
    @NotEmpty(message = "At least one Product Generic is required")
    private List<@Valid ProductGenericCreateRequest> productGenerics;
    @NotNull(message = "Product Category is required")
    private Long categoryId;
}
