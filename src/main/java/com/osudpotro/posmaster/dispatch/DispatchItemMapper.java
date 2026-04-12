package com.osudpotro.posmaster.dispatch;

import com.osudpotro.posmaster.manufacturer.Manufacturer;
import com.osudpotro.posmaster.product.Product;
import com.osudpotro.posmaster.product.ProductDetail;
import com.osudpotro.posmaster.producttype.ProductType;
import com.osudpotro.posmaster.purchase.PurchaseDetail;
import com.osudpotro.posmaster.variantunit.VariantUnit;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DispatchItemMapper {
    //Mapping Here
    //Entity → DTO
    public DispatchItemDto toDto(DispatchItem dispatchItem) {
        DispatchItemDto dto = new DispatchItemDto();
        dto.setId(dispatchItem.getId());
        if (dispatchItem.getProduct() != null) {
            Product product = dispatchItem.getProduct();
            dto.setProductId(product.getId());
            dto.setProductName(product.getProductName());
            dto.setProductCode(product.getProductCode());
            dto.setProductBarCode(product.getProductBarCode());
            if(product.getProductType()!=null){
                ProductType productType=product.getProductType();
                dto.setProductTypeId(productType.getId());
                dto.setProductTypeName(productType.getName());
            }
            if(product.getManufacturer()!=null){
                Manufacturer manufacturer=product.getManufacturer();
                dto.setManufacturerId(manufacturer.getId());
                dto.setManufacturerName(manufacturer.getName());
            }
        }
        if(dispatchItem.getProductDetail()!=null){
            ProductDetail productDetail=dispatchItem.getProductDetail();
            dto.setProductDetailId(productDetail.getId());
            dto.setProductDetailCode(productDetail.getProductDetailCode());
            dto.setProductDetailBarCode(productDetail.getProductDetailBarCode());
            dto.setProductDetailSku(productDetail.getProductDetailSku());
            if(productDetail.getSize()!=null){
               VariantUnit size=productDetail.getSize();
               dto.setSizeId(size.getId());
               dto.setSizeName(size.getName());
            }
            dto.setDispatchQty(dispatchItem.getDispatchQty());
        }
        if(dispatchItem.getPurchaseDetail()!=null){
            PurchaseDetail purchaseDetail=dispatchItem.getPurchaseDetail();
            dto.setPurchasePrice(purchaseDetail.getPurchasePrice());
        }
        dto.setDispatchQty(dispatchItem.getDispatchQty());
        dto.setRequestedQty(dispatchItem.getRequestedQty());
        dto.setUpdatedQty(dispatchItem.getUpdatedQty());
        return dto;
    }
//    private BigDecimal purchasePrice;
//    private Integer dispatchQty;
}
