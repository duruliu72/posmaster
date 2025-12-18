package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.category.Category;
import com.osudpotro.posmaster.variantunit.VariantUnit;
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
//        if (productDetail.getParentSize() != null) {
//            VariantUnitDto parentSizeDto = new VariantUnitDto();
//            parentSizeDto.setId(productDetail.getParentSize().getId());
//            parentSizeDto.setName(productDetail.getParentSize().getName());
//            productDetailDto.setParentSize(parentSizeDto);
//        }
        if (productDetail.getParentProductDetail() != null) {
            ProductDetailDto parentProductDetailDto = new ProductDetailDto();
            parentProductDetailDto.setId(productDetail.getParentProductDetail().getId());
            productDetailDto.setParentProductDetail(parentProductDetailDto);
        }
        List<Long> productDetailIds = new ArrayList<>();
        productDetailDto.setProductDetailIds(getProductDetailWithParents(productDetail, productDetailIds));

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



