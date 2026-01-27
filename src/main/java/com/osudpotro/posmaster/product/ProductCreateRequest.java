package com.osudpotro.posmaster.product;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ProductCreateRequest {
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
    private List<ProductDetailCreateRequest> details;
    //    @NotEmpty(message = "At least one Product Generic is required")
//    private List<@Valid ProductGenericCreateRequest> productGenerics;
    private List<ProductGenericCreateRequest> productGenerics;
//    @NotNull(message = "Product Category is required")
    private Long categoryId;
    private String tags;
    private String seoPageName;
    private String metaTitle;
    private String metaKeywords;
    private String metaDescription;
}
