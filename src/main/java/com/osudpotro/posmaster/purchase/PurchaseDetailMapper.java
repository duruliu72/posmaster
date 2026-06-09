package com.osudpotro.posmaster.purchase;

import com.osudpotro.posmaster.product.ProductDetailMapper;
import com.osudpotro.posmaster.product.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PurchaseDetailMapper {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductDetailMapper detailMapper;

    //Mapping Here
    //Entity → DTO
    public PurchaseDetailDto toDto(PurchaseDetail pd) {
        PurchaseDetailDto pdDto = new PurchaseDetailDto();
        pdDto.setPurchase(null);
        pdDto.setPurchaseRequisition(null);
        pdDto.setPurchaseRequisitionItem(null);
        pdDto.setProduct(null);
        pdDto.setProductDetail(null);
        pdDto.setPurchasePrice(pd.getPurchasePrice());
        pdDto.setMrpPrice(pd.getMrpPrice());
        pdDto.setPurchaseDiscount(pd.getPurchaseDiscount());
        pdDto.setPurchaseQty(pd.getPurchaseQty());
        pdDto.setGiftOrBonusQty(pd.getGiftOrBonusQty());
        pdDto.setAtomQty(pd.getAtomQty());
        pdDto.setPurchaseBarCode(pd.getPurchaseBarCode());
        pdDto.setProductionBatchNo(pd.getProductionBatchNo());
        return  pdDto;
    }
}
