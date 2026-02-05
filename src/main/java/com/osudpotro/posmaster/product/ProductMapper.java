package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.brand.Brand;
import com.osudpotro.posmaster.brand.BrandDto;
import com.osudpotro.posmaster.category.Category;
import com.osudpotro.posmaster.category.CategoryDto;
import com.osudpotro.posmaster.category.CustomCategoryMapper;
import com.osudpotro.posmaster.generic.GenericDto;
import com.osudpotro.posmaster.genericunit.GenericUnitDto;
import com.osudpotro.posmaster.manufacturer.Manufacturer;
import com.osudpotro.posmaster.manufacturer.ManufacturerDto;
import com.osudpotro.posmaster.multimedia.MultimediaDto;
import com.osudpotro.posmaster.producttype.ProductType;
import com.osudpotro.posmaster.producttype.ProductTypeDto;
import com.osudpotro.posmaster.variantunit.VariantUnitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMapper {
    @Autowired
    private CustomCategoryMapper customCategoryMapper;

    //Mapping Here
    //Entity → DTO
    public ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setProductName(product.getProductName());
        productDto.setProductCode(product.getProductCode());
        productDto.setProductBarCode(product.getProductBarCode());
        productDto.setProductSku(product.getProductSku());
        productDto.setTags(product.getTags());
        productDto.setSeoPageName(product.getSeoPageName());
        productDto.setMetaTitle(product.getMetaTitle());
        productDto.setMetaKeywords(product.getMetaKeywords());
        productDto.setMetaDescription(product.getMetaDescription());
        if(product.getPurchaseProductUnit()!=null){
            ProductDetailDto purchaseProductUnitDto = setForMapProductDetails(product.getPurchaseProductUnit());
            productDto.setPurchaseProductUnit(purchaseProductUnitDto);
        }
        productDto.setIsPrescribeNeeded(product.getIsPrescribeNeeded());
        productDto.setDescription(product.getDescription());
        // Manufacturer
        Manufacturer manufacturer = product.getManufacturer();
        if (manufacturer != null) {
            ManufacturerDto manufacturerDto = new ManufacturerDto();
            manufacturerDto.setId(manufacturer.getId());
            manufacturerDto.setName(manufacturer.getName());
            productDto.setManufacturer(manufacturerDto);
        }
        //Category
        Category category = product.getCategory();
        if (category != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            productDto.setCategory(customCategoryMapper.toDto(category));
        }
        // Brand
        Brand brand = product.getBrand();
        if (brand != null) {
            BrandDto brandDto = new BrandDto();
            brandDto.setId(brand.getId());
            brandDto.setName(brand.getName());
            productDto.setBrand(brandDto);
        }
        //Product Type
        ProductType productType = product.getProductType();
        if (productType != null) {
            ProductTypeDto productTypeDto = new ProductTypeDto();
            productTypeDto.setId(productType.getId());
            productTypeDto.setName(productType.getName());
            productDto.setProductType(productTypeDto);
        }
        if (product.getMedia() != null && product.getMedia().getImageUrl() != null) {
            MultimediaDto multimediaDto = new MultimediaDto();
            multimediaDto.setId(product.getMedia().getId());
            multimediaDto.setName(product.getMedia().getName());
            multimediaDto.setImageUrl(product.getMedia().getImageUrl());
            multimediaDto.setMediaType(product.getMedia().getMediaType());
            productDto.setMedia(multimediaDto);
        }
        // PRODUCT GENERICS
        List<ProductGenericDto> productGenerics = new ArrayList<>();
        for (ProductGeneric productGeneric : product.getProductGenerics()) {
            ProductGenericDto productGenericDto = new ProductGenericDto();
            GenericDto genericDto = new GenericDto();
            genericDto.setId(productGeneric.getGeneric().getId());
            genericDto.setName(productGeneric.getGeneric().getName());
            productGenericDto.setGeneric(genericDto);
            productGenericDto.setDose(productGeneric.getDose());
            GenericUnitDto genericUnitDto = new GenericUnitDto();
            genericUnitDto.setId(productGeneric.getGenericUnit().getId());
            genericUnitDto.setName(productGeneric.getGenericUnit().getName());
            productGenericDto.setGenericUnit(genericUnitDto);
            productGenerics.add(productGenericDto);
        }
        productDto.setProductGenerics(productGenerics);
        // Product Details
        List<ProductDetailDto> details = new ArrayList<>();
        for (ProductDetail detail : product.getDetails()) {
            ProductDetailDto detailDto = setForMapProductDetails(detail);
//            For Parent Product Detail
            if (detail.getParentProductDetail() != null) {
                ProductDetailDto parentProductDetailDto = setForMapProductDetails(detail.getParentProductDetail());
                detailDto.setParentProductDetail(parentProductDetailDto);
            }
            details.add(detailDto);
        }
        productDto.setDetails(details);
        return productDto;
    }
    private ProductDetailDto setForMapProductDetails(ProductDetail detail){
        ProductDetailDto detailDto = new ProductDetailDto();
        detailDto.setId(detail.getId());
        detailDto.setProductDetailCode(detail.getProductDetailCode());
        detailDto.setProductDetailBarCode(detail.getProductDetailBarCode());
        detailDto.setProductDetailBarCodeImage(detail.generateProductDetailsBarCodeImage(null, null));
        detailDto.setProductDetailSku(detail.getProductDetailSku());
        detailDto.setMrpPrice(detail.getMrpPrice());
        detailDto.setSellPrice(detail.getSellPrice());
        if (detail.getSize() != null) {
            VariantUnitDto size = new VariantUnitDto();
            size.setId(detail.getSize().getId());
            size.setName(detail.getSize().getName());
            detailDto.setSize(size);
        }
        detailDto.setBulkSize(detail.getBulkSize());
        detailDto.setAtomQty(detail.getAtomQty());
        if(detail.getColor()!=null){
            VariantUnitDto color = new VariantUnitDto();
            color.setId(detail.getColor().getId());
            color.setName(detail.getColor().getName());
            detailDto.setColor(color);
        }
        return detailDto;
    }
}
