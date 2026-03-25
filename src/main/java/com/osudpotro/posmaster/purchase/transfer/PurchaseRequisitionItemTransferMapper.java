package com.osudpotro.posmaster.purchase.transfer;

import com.osudpotro.posmaster.product.ProductDetailMapper;
import com.osudpotro.posmaster.product.ProductMapper;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionItem;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PurchaseRequisitionItemTransferMapper {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductDetailMapper detailMapper;
    //Mapping Here
    //Entity → DTO
    public PurchaseRequisitionItemTransferDto toDto(PurchaseRequisitionItemTransfer priTransfer) {
        PurchaseRequisitionItemTransferDto priTransferDto = new PurchaseRequisitionItemTransferDto();
        var prTransfer=priTransfer.getPurchaseRequisitionTransfer();
        var pri=priTransfer.getPurchaseRequisitionItem();
        priTransferDto.setId(priTransfer.getId());
        priTransferDto.setPurchaseRequisitionTransferId(prTransfer.getId());
        priTransferDto.setPurchaseRequisitionItemId(pri.getId());
        priTransferDto.setProduct(productMapper.toDto(priTransfer.getProduct()));
        priTransferDto.setProductDetail(detailMapper.toDto(priTransfer.getProductDetail()));
        priTransferDto.setPurchaseQty(priTransfer.getPurchaseQty());
        priTransferDto.setGiftOrBonusQty(priTransfer.getGiftOrBonusQty());
        priTransferDto.setPurchasePrice(priTransfer.getPurchasePrice());
        priTransferDto.setMrpPrice(priTransfer.getMrpPrice());
        priTransferDto.setDiscount(priTransfer.getDiscountPrice());
        if (priTransfer.getPurchaseQty() != null) {
            priTransferDto.setPurchaseLinePrice(priTransfer.getPurchaseLinePrice());
        }
        if (priTransfer.getGiftOrBonusQty() != null) {
            priTransferDto.setGiftOrBonusLinePrice(priTransfer.getGiftOrBonusLinePrice());
        }
        return priTransferDto;
    }

}
