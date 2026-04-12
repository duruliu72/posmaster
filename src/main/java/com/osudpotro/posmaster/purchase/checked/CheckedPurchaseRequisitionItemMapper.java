package com.osudpotro.posmaster.purchase.checked;

import com.osudpotro.posmaster.product.ProductDetailMapper;
import com.osudpotro.posmaster.product.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckedPurchaseRequisitionItemMapper {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductDetailMapper detailMapper;
    //Mapping Here
    //Entity → DTO
    public CheckedPurchaseRequisitionItemDto toDto(CheckedPurchaseRequisitionItem cprItem) {
        CheckedPurchaseRequisitionItemDto cprItemDto = new CheckedPurchaseRequisitionItemDto();
        var cpr=cprItem.getCheckedPurchaseRequisition();
        var prItem=cprItem.getPurchaseRequisitionItem();
        cprItemDto.setId(cprItem.getId());
        cprItemDto.setCheckedPurchaseRequisitionId(cpr.getId());
        cprItemDto.setPurchaseRequisitionItemId(prItem.getId());
        cprItemDto.setProduct(productMapper.toDto(cprItem.getProduct()));
        cprItemDto.setProductDetail(detailMapper.toDto(cprItem.getProductDetail()));
        cprItemDto.setPurchaseQty(cprItem.getPurchaseQty());
        cprItemDto.setGiftOrBonusQty(cprItem.getGiftOrBonusQty());
        cprItemDto.setPurchasePrice(cprItem.getPurchasePrice());
        cprItemDto.setMrpPrice(cprItem.getMrpPrice());
        cprItemDto.setDiscount(cprItem.getDiscountPrice());
        if (cprItem.getPurchaseQty() != null) {
            cprItemDto.setPurchaseLinePrice(cprItem.getPurchaseLinePrice());
        }
        if (cprItem.getGiftOrBonusQty() != null) {
            cprItemDto.setGiftOrBonusLinePrice(cprItem.getGiftOrBonusLinePrice());
        }
        cprItemDto.setProductionBatchNo(cprItem.getProductionBatchNo());
        cprItemDto.setManufactureDate(cprItem.getManufactureDate());
        cprItemDto.setExpiredDate(cprItem.getExpiredDate());
        cprItemDto.setIsAddedToInventory(cprItem.getIsAddedToInventory());
        return cprItemDto;
    }

}
