package com.osudpotro.posmaster.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProductUpdateRequest {
    @NotBlank(message = "Product name is required")
    private String ProductName;
    private String description;
    private Boolean isPrescribeNeeded;
//    @NotNull(message = "Product manufacturer is required")
    private Long manufacturerId;
//    @NotNull(message = "Product Brand is required")
    private Long brandId;
//    @NotNull(message = "Product Type is required")
    private Long ProductTypeId;
//    @NotEmpty(message = "At least one product Details is required")
    private List<@Valid ProductDetailUpdateRequest> details;
//    @NotEmpty(message = "At least one Product Generic is required")
    private List<@Valid ProductGenericUpdateRequest> productGenerics;
    @NotNull(message = "Product Category is required")
    private Long categoryId;
    private String tags;
    private Long multimediaId;
    private String seoPageName;
    private String metaTitle;
    private String metaKeywords;
    private String metaDescription;

}
