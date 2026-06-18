package com.osudpotro.posmaster.purchase;


import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.branch.BranchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PurchaseMapper {
    @Autowired
    private PurchaseDetailMapper pdMapper;
    public PurchaseDto toDto(Purchase purchase) {
        PurchaseDto purchaseDto=new PurchaseDto();
        purchaseDto.setPurchaseRef(purchase.getPurchaseRef());
        purchaseDto.setPurchaseRequisition(null);
        purchaseDto.setCheckedPurchaseRequisition(null);
        purchaseDto.setRequsitionRef(purchase.getRequsitionRef());
        purchaseDto.setPurchaseType(purchase.getPurchaseType());
        purchaseDto.setOrganization(null);
        if(purchase.getBranch()!=null){
            Branch branch=purchase.getBranch();
            BranchDto branchDto=new BranchDto();
            branchDto.setId(branch.getId());
            branchDto.setName(branch.getName());
            purchaseDto.setBranch(branchDto);
        }
        purchaseDto.setWarehouse(null);
        purchaseDto.setSupplier(null);
        purchaseDto.setPurchaseBatchNo(purchase.getPurchaseBatchNo());
        purchaseDto.setOverallDiscount(purchase.getOverallDiscount());
        purchaseDto.setPurchaseInvoices(purchase.getPurchaseInvoices());
        purchaseDto.setOrderRefs(purchase.getOrderRefs());
        purchaseDto.setAddedBy(null);
        purchaseDto.setPurchaseAt(purchase.getPurchaseAt());
        purchaseDto.setUpdatedBy(null);
        purchaseDto.setUpdatedAt(null);
        purchaseDto.setPurchaseStatus(purchase.getPurchaseStatus());
//        For Exttra Properties
        purchaseDto.setTotalQty(purchase.getTotalQty());
        purchaseDto.setTotalPrice(purchase.getTotalPrice());
        return purchaseDto;
    }
    public PurchaseDto toMaxDto(Purchase p) {
        PurchaseDto pDto = toDto(p);
        if(p.getItems()!=null){
            List<PurchaseDetailDto> items = new ArrayList<>();
            for (PurchaseDetail pd:p.getItems()){
                items.add(pdMapper.toDto(pd));
            }
            pDto.setItems(items);
        }
        return  pDto;
    }
}
