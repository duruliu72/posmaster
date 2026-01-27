package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.brand.Brand;
import com.osudpotro.posmaster.brand.BrandDto;
import com.osudpotro.posmaster.category.Category;
import com.osudpotro.posmaster.category.CategoryDto;
import com.osudpotro.posmaster.manufacturer.Manufacturer;
import com.osudpotro.posmaster.manufacturer.ManufacturerDto;
import com.osudpotro.posmaster.producttype.ProductType;
import com.osudpotro.posmaster.producttype.ProductTypeDto;
import com.osudpotro.posmaster.variantunit.VariantUnitDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDetailMapper {
    //Mapping Here
    //Entity → DTO
    public ProductDetailDto toDto(ProductDetail productDetail) {
        ProductDetailDto productDetailDto = new ProductDetailDto();
        productDetailDto.setId(productDetail.getId());
        productDetailDto.setProductDetailCode(productDetail.getProductDetailCode());
        productDetailDto.setProductDetailBarCode(productDetail.getProductDetailBarCode());
        productDetailDto.setProductDetailBarCodeImage(productDetail.generateProductDetailsBarCodeImage(null, null));
        productDetailDto.setProductDetailSku(productDetail.getProductDetailSku());
        productDetailDto.setAtomQty(productDetail.getAtomQty());
        productDetailDto.setBulkSize(productDetail.getBulkSize());
        VariantUnitDto sizeDto = new VariantUnitDto();
        sizeDto.setId(productDetail.getSize().getId());
        sizeDto.setName(productDetail.getSize().getName());
        productDetailDto.setSize(sizeDto);
        if (productDetail.getColor() != null) {
            VariantUnitDto colorDto = new VariantUnitDto();
            colorDto.setId(productDetail.getColor().getId());
            colorDto.setName(productDetail.getColor().getName());
            productDetailDto.setColor(colorDto);
        }
        if (productDetail.getParentProductDetail() != null) {
            ProductDetailDto parentProductDetailDto = new ProductDetailDto();
            parentProductDetailDto.setId(productDetail.getParentProductDetail().getId());
            productDetailDto.setParentProductDetail(parentProductDetailDto);
        }
        productDetailDto.setOldPrice(productDetail.getOldPrice());
        productDetailDto.setRegularPrice(productDetail.getRegularPrice());
        productDetailDto.setPurchasePrice(productDetail.getPurchasePrice());
        List<Long> productDetailIds = new ArrayList<>();
        productDetailDto.setProductDetailIds(getProductDetailWithParents(productDetail, productDetailIds));
        if (productDetail.getProduct() != null) {
            ProductDto productDto = new ProductDto();
            productDto.setId(productDetail.getProduct().getId());
            productDto.setProductName(productDetail.getProduct().getProductName());
            productDto.setProductCode(productDetail.getProduct().getProductCode());
            Category category=productDetail.getProduct().getCategory();
            if (category != null) {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setId(category.getId());
                categoryDto.setName(category.getName());
                productDto.setCategory(categoryDto);
            }
            Brand brand=productDetail.getProduct().getBrand();
            if(brand!=null){
                BrandDto brandDto=new BrandDto();
                brandDto.setId(brandDto.getId());
                brandDto.setName(brandDto.getName());
                productDto.setBrand(brandDto);
            }
            ProductType productType=productDetail.getProduct().getProductType();
            if(productType!=null){
                ProductTypeDto productTypeDto=new ProductTypeDto();
                productTypeDto.setId(productType.getId());
                productTypeDto.setName(productType.getName());
                productDto.setProductType(productTypeDto);
            }
            Manufacturer manufacturer=productDetail.getProduct().getManufacturer();
            if(manufacturer!=null){
                ManufacturerDto manufacturerDto=new ManufacturerDto();
                manufacturerDto.setId(manufacturer.getId());
                manufacturerDto.setName(manufacturer.getName());
                productDto.setManufacturer(manufacturerDto);
            }
            productDetailDto.setProduct(productDto);
        }
        return productDetailDto;
    }

    private List<Long> getProductDetailWithParents(ProductDetail productDetail, List<Long> productDetailIds) {
        if (productDetail.getParentProductDetail() != null) {
            productDetailIds.add(productDetail.getId());
            getProductDetailWithParents(productDetail.getParentProductDetail(), productDetailIds);
        } else {
            productDetailIds.add(productDetail.getId());
        }
        return productDetailIds;
    }
}



