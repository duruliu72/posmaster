package com.osudpotro.posmaster.inventory;

import com.osudpotro.posmaster.common.EntityNotFoundException;
import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository invRepo;
    @Autowired
    private AuthService authService;
    @Autowired
    InventoryMapper invMapper;

    List<InventoryByBatchNo> getInvListByBatch(Long productId, Long productDetailId) {
        var authUser = authService.getCurrentUser();
        return invRepo.getInvListByBatch(authUser.getBranch().getId(),productId, productDetailId);
    }

    public Page<InventoryDto> filterEntities(InventoryFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return invRepo.findAll(InventorySpecification.filter(filter, authUser), pageable).map(invMapper::toDto);
    }

    public Page<InventoryByPurchaseBarcode> filterInvGroupByPurchaseBarCodeFromAuthBranch(InventoryFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return invRepo.filterInvGroupByPurchaseBarCodeFromAuthBranch(authUser.getBranch().getId(), filter.getProductName(), pageable);
    }

    public InventoryByPurchaseBarcode getInvGroupByPurchaseBarCodeFromAuthBranch(InventoryFilter filter) {
        var authUser = authService.getCurrentUser();
        InventoryByPurchaseBarcode invGroupByPurchaseBarCode = invRepo.findInvGroupByPurchaseBarCodeFromAuthBranch(authUser.getBranch().getId(), filter.getPurchaseBarCode()).orElse(null);
        if (invGroupByPurchaseBarCode == null) {
            throw new EntityNotFoundException("Item Not Found");
        }
        return invGroupByPurchaseBarCode;
    }

    public Page<InventoryByBatchNo> filterInvGroupBatchByAuthBranch(InventoryFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return invRepo.filterInvGroupBatchByBranch(authUser.getBranch().getId(), "", pageable);
    }
    public Page<InventoryByProductDetail> filterInvGroupProductDetailByAuthBranch(InventoryFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return invRepo.filterInvGroupProductDetailByBranch(authUser.getBranch().getId(), filter.getSearchKey(), pageable);
    }
    public Page<InventoryByProductDetail> filterInvGroupProductDetailByBranch(InventoryFilter filter, Pageable pageable) {
        return invRepo.filterInvGroupProductDetailByBranch(filter.getBranchId(), filter.getSearchKey(), pageable);
    }
}
