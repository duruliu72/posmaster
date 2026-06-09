package com.osudpotro.posmaster.purchase;

import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionDto;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepo;
    public List<PurchaseRequisitionDto> getAllPurchaseRequisitions() {
//        return prRepo.findAll()
//                .stream()
//                .map(purchaseRequisitionMapper::toDto)
//                .toList();
        return null;
    }

    public Page<PurchaseRequisitionDto> filterPrEntities(PurchaseRequisitionFilter filter, Pageable pageable) {
//        var authUser = authService.getCurrentUser();
//        return prRepo.findAll(PurchaseRequisitionSpecification.filter(filter, authUser), pageable).map(purchaseRequisitionMapper::toDto);
        return null;
    }

}
