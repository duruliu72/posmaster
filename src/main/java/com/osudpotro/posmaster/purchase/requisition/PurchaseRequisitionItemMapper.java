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
    public PurchaseRequisitionItemDto toDto(PurchaseRequisitionItem prItem) {
        PurchaseRequisitionItemDto prItemDto = new PurchaseRequisitionItemDto();
        prItemDto.setId(prItem.getId());
        prItemDto.setProduct(productMapper.toDto(prItem.getProduct()));
        prItemDto.setProductDetail(detailMapper.toDto(prItem.getProductDetail()));
        prItemDto.setPurchaseQty(prItem.getPurchaseQty());
        prItemDto.setActualQty(prItem.getActualQty());
        prItemDto.setGiftQty(prItem.getGiftQty());
        prItemDto.setAddableStatus(prItem.getAddableStatus());
        if (prItem.getProductDetail() != null) {
            if (prItem.getProductDetail().getPurchasePrice() != null) {
                prItemDto.setPurchasePrice(prItem.getProductDetail().getPurchasePrice());
                prItemDto.setPurchaseLinePrice(prItem.getProductDetail().getPurchasePrice() * prItem.getPurchaseQty());
                if (prItem.getActualQty() != null) {
                    prItemDto.setActualLinePrice(prItem.getProductDetail().getPurchasePrice() * prItem.getActualQty());
                }
                if (prItem.getGiftQty() != null) {
                    prItemDto.setGiftLinePrice(prItem.getProductDetail().getPurchasePrice() * prItem.getGiftQty());
                }
            }
        }
        return prItemDto;
    }

}
