package com.osudpotro.posmaster.inventory;

import com.osudpotro.posmaster.common.PagedResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService invSummaryService;

    @PostMapping("/filter")
    public PagedResponse<InventoryDto> filterEntities(
            @RequestBody InventoryFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "purchaseBatchNo") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryDto> result = invSummaryService.filterEntities(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/filter-group-by-purchase-barcode-from-auth-branch")
    public PagedResponse<InventoryByPurchaseBarcode> filterInvGroupPurchaseBarcodeByAuthBranch(
            @RequestBody InventoryFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "purchaseBatchNo") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryByPurchaseBarcode> result = invSummaryService.filterInvGroupByPurchaseBarCodeFromAuthBranch(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/filter-group-batch-by-auth-branch")
    public PagedResponse<InventoryByBatchNo> filterInvGroupBatchByAuthBranch(
            @RequestBody InventoryFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "purchaseBatchNo") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryByBatchNo> result = invSummaryService.filterInvGroupBatchByAuthBranch(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/filter-group-product-detail-by-auth-branch")
    public PagedResponse<InventoryByProductDetail> filterInvGroupProductDetailByAuthBranch(
            @RequestBody InventoryFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "purchaseBatchNo") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryByProductDetail> result = invSummaryService.filterInvGroupProductDetailByAuthBranch(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/filter-group-product-detail-by-branch")
    public PagedResponse<InventoryByProductDetail> filterInvGroupProductDetailByBranch(
            @RequestBody InventoryFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "purchaseBatchNo") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryByProductDetail> result = invSummaryService.filterInvGroupProductDetailByBranch(filter, pageable);
        return new PagedResponse<>(result);
    }
}
