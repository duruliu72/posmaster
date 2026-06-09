package com.osudpotro.posmaster.purchase;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {
    @Autowired
    private PurchaseDetailMapper pdMapper;
    public PurchaseDto toDto(Purchase purchase) {
        PurchaseDto purchaseDto=new PurchaseDto();

        return purchaseDto;
    }
}
