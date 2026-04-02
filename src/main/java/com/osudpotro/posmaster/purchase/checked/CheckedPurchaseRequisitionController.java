package com.osudpotro.posmaster.purchase.checked;

import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionException;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsOnTripDeliveryException;
import com.osudpotro.posmaster.tms.vechile.DuplicateVehicleException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/checked-purchase-requisitions")
public class CheckedPurchaseRequisitionController {
    private final CheckedPurchaseRequisitionService cprService;

    @GetMapping
    public List<CheckedPurchaseRequisitionDto> getAllEntities() {
        return cprService.getAllEntities();
    }

    @PostMapping("/filter")
    public PagedResponse<CheckedPurchaseRequisitionDto> filterEntities(
            @RequestBody CheckedPurchaseRequisitionFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CheckedPurchaseRequisitionDto> result = cprService.filterEntities(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/filter-by-branch")
    public PagedResponse<CheckedPurchaseRequisitionDto> filterEntitiesByBranch(
            @RequestBody CheckedPurchaseRequisitionFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CheckedPurchaseRequisitionDto> result = cprService.filterEntitiesByBranch(filter, pageable);
        return new PagedResponse<>(result);
    }

    //  For Checked Purchase Requisition item Pagination
    @PostMapping("/{id}/filter")
    public CheckedPurchaseRequisitionWithItemPageResponse getEntityWithFilterItems(@PathVariable Long id,
                                                                                   @RequestBody CheckedPurchaseRequisitionFilter filter,
                                                                                   @RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "10") int size,
                                                                                   @RequestParam(defaultValue = "id") String sortBy,
                                                                                   @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return cprService.getEntityWithFilterItems(id, pageable, filter);
    }

    @PostMapping("/{id}/update-by-admin")
    public CheckedPurchaseRequisitionDto updateByAdmin(
            @PathVariable(name = "id") Long id,
            @RequestBody CheckedPurchaseRequisitionRequest request) {
        return cprService.updateByAdmin(id, request);
    }

    //    @PostMapping("/{checkedPurchaseRequisitionId}/update-from-branch")
//    public CheckedPurchaseRequisitionDto updateFromMedicineCorner(@PathVariable Long purchaseRequisitionTransferId, @RequestBody UpdateFromMedicineCornerRequest request) {
//        return prtService.updateFromMedicineCorner(purchaseRequisitionTransferId, request);
//    }
    @PostMapping("/{cprId}/update-item-from-branch/{cprItemId}")
    public CheckedPurchaseRequisitionDto updateItemFromBranch(@PathVariable Long cprId, @PathVariable Long cprItemId, @RequestBody UpdateFromBranchRequest request) {
        return cprService.updateItemFromBranch(cprId, cprItemId, request);
    }
    @PostMapping("/{cprId}/add-to-inventory")
    public CheckedPurchaseRequisitionDto addToInventory(@PathVariable Long cprId, @RequestBody AddToInventoryRequest request) {
        return cprService.addToInventory(cprId, request);
    }

    @ExceptionHandler(CheckedPurchaseRequisitionNotFoundException.class)
    public ResponseEntity<Void> handleCheckedPurchaseRequisitionNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DuplicateVehicleException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateVehicleException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }

    @ExceptionHandler(PurchaseRequisitionException.class)
    public ResponseEntity<Map<String, String>> handlePurchaseRequisitionException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }

    @ExceptionHandler(GoodsOnTripDeliveryException.class)
    public ResponseEntity<Map<String, String>> handleGoodsOnTripDeliveryException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }
}
