package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.brand.BrandDto;
import com.osudpotro.posmaster.category.CategoryDto;
import com.osudpotro.posmaster.manufacturer.ManufacturerDto;
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
    private String description;
    private Long manufacturerId;
    private ManufacturerDto manufacturer;
    private Long brandId;
    private BrandDto brand;
    private Long categoryId;
    private CategoryDto category;
    private Long productTypeId;
    private ProductTypeDto productType;
    private List<ProductDetailDto> details;
    private List<ProductGenericDto> productGenerics;
}
