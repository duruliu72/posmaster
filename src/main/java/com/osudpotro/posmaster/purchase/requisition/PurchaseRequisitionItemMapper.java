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
        prItemDto.setPurchaseProductUnit(detailMapper.toDto(prItem.getPurchaseProductUnit()));
        prItemDto.setPurchaseQty(prItem.getPurchaseQty());
        prItemDto.setActualQty(prItem.getActualQty());
        prItemDto.setGiftOrBonusQty(prItem.getGiftOrBonusQty());
        prItemDto.setAddableStatus(prItem.getAddableStatus());
        prItemDto.setPurchasePrice(prItem.getPurchasePrice());
        prItemDto.setMrpPrice(prItem.getMrpPrice());
        prItemDto.setDiscount(prItem.getDiscountPrice());
        if (prItem.getPurchaseQty() != null) {
            prItemDto.setPurchaseLinePrice(prItem.getPurchaseLinePrice());
        }
        if (prItem.getActualQty() != null) {
            prItemDto.setActualLinePrice(prItem.getActualLinePrice());
        }
        if (prItem.getGiftOrBonusQty() != null) {
            prItemDto.setGiftOrBonusLinePrice(prItem.getGiftOrBonusLinePrice());
        }
        return prItemDto;
    }

}
