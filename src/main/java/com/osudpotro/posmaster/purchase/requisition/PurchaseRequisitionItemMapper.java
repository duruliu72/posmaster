package com.osudpotro.posmaster.purchase.requisition;

import com.osudpotro.posmaster.product.ProductDetailMapper;
import com.osudpotro.posmaster.product.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PurchaseRequisitionItemMapper {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductDetailMapper detailMapper;
    //Mapping Here
    //Entity → DTO
    public PurchaseRequisitionItemDto toDto(PurchaseRequisitionItem purchaseRequisitionItem) {
        PurchaseRequisitionItemDto purchaseRequisitionItemDto = new PurchaseRequisitionItemDto();
        purchaseRequisitionItemDto.setId(purchaseRequisitionItem.getId());
        purchaseRequisitionItemDto.setProduct(productMapper.toDto(purchaseRequisitionItem.getProduct()));
        purchaseRequisitionItemDto.setProductDetail(detailMapper.toDto(purchaseRequisitionItem.getProductDetail()));
        purchaseRequisitionItemDto.setPurchaseQty(purchaseRequisitionItem.getPurchaseQty());
        if(purchaseRequisitionItem.getProductDetail()!=null){
            if(purchaseRequisitionItem.getProductDetail().getPurchasePrice()!=null){
                purchaseRequisitionItemDto.setPurchasePrice(purchaseRequisitionItem.getProductDetail().getPurchasePrice());
                purchaseRequisitionItemDto.setPurchaseLinePrice(purchaseRequisitionItem.getProductDetail().getPurchasePrice()*purchaseRequisitionItem.getPurchaseQty());
            }

        }
        return purchaseRequisitionItemDto;
    }

}
