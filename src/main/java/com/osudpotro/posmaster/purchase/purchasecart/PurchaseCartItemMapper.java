package com.osudpotro.posmaster.purchase.purchasecart;

import com.osudpotro.posmaster.product.ProductDetailMapper;
import com.osudpotro.posmaster.product.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PurchaseCartItemMapper {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductDetailMapper detailMapper;
    //Mapping Here
    //Entity → DTO
    public PurchaseCartItemDto toDto(PurchaseCartItem purchaseCartItem) {
        PurchaseCartItemDto purchaseCartItemDto = new PurchaseCartItemDto();
        purchaseCartItemDto.setId(purchaseCartItem.getId());
        purchaseCartItemDto.setProduct(productMapper.toDto(purchaseCartItem.getProduct()));
        purchaseCartItemDto.setProductDetail(detailMapper.toDto(purchaseCartItem.getProductDetail()));
        purchaseCartItemDto.setPurchaseQty(purchaseCartItem.getPurchaseQty());
        if(purchaseCartItem.getProductDetail()!=null){
            if(purchaseCartItem.getProductDetail().getPurchasePrice()!=null){
                purchaseCartItemDto.setPurchasePrice(purchaseCartItem.getProductDetail().getPurchasePrice());
                purchaseCartItemDto.setPurchaseLinePrice(purchaseCartItem.getProductDetail().getPurchasePrice()*purchaseCartItem.getPurchaseQty());
            }

        }
        return purchaseCartItemDto;
    }
}
