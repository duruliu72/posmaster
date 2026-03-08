package com.osudpotro.posmaster.purchase.transfer;
import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/purchase-requisition-transfers")
public class PurchaseRequisitionTransferController {
    private final PurchaseRequisitionTransferService prtService;
    @GetMapping
    public List<PurchaseRequisitionTransferDto> getAllEntities() {
        return prtService.getAllEntities();
    }
    @PostMapping("/filter")
    public PagedResponse<PurchaseRequisitionTransferDto> filterEntities(
            @RequestBody PurchaseRequisitionTransferFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PurchaseRequisitionTransferDto> result = prtService.filterEntities(filter, pageable);
        return new PagedResponse<>(result);
    }
    @PostMapping("/{purchaseRequisitionId}/filter")
    public PagedResponse<PurchaseRequisitionTransferDto> filterEntitiesByPurchaseRequisition(
            @PathVariable Long purchaseRequisitionId,
            @RequestBody PurchaseRequisitionTransferFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        filter.setPurchaseRequisitionId(purchaseRequisitionId);
        Page<PurchaseRequisitionTransferDto> result = prtService.filterEntitiesByPurchaseRequisition(filter, pageable);
        return new PagedResponse<>(result);
    }
    @PostMapping("/{purchaseRequisitionTransferId}/filter-items")
    public PurchaseRequisitionTransferWithItemPageResponse filterEntitiesWithItemPage(
            @PathVariable Long purchaseRequisitionTransferId,
            @RequestBody PurchaseRequisitionItemTransferFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return prtService.filterEntitiesWithItemPage(purchaseRequisitionTransferId, filter,pageable);
    }
    @GetMapping("/{id}")
    public PurchaseRequisitionTransferDto getEntity(@PathVariable Long id) {
        return prtService.getEntity(id);
    }

}
