package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.brand.BrandDto;
import com.osudpotro.posmaster.category.CategoryDto;
import com.osudpotro.posmaster.manufacturer.ManufacturerDto;
import com.osudpotro.posmaster.multimedia.MultimediaDto;
import com.osudpotro.posmaster.producttype.ProductTypeDto;
import lombok.Data;

import java.util.List;


@Data
public class ProductDto {
    private Long id;
    private String productName;
    private String productCode;
    private String productBarCode;
    private String productSku;
    private String tags;
    private String description;
    private Boolean isPrescribeNeeded;
    private ManufacturerDto manufacturer;
    private BrandDto brand;
    private CategoryDto category;
    private ProductTypeDto productType;
    private List<ProductDetailDto> details;
    private List<ProductGenericDto> productGenerics;
    private String seoPageName;
    private String metaTitle;
    private String metaKeywords;
    private String metaDescription;
    private MultimediaDto media;
    private ProductDetailDto purchaseProductUnit;
}
