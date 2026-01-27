package com.osudpotro.posmaster.purchase.requisition;

import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.requisition.RequisitionUpdateException;
import com.osudpotro.posmaster.requisition.RequsitionOnPathNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/purchase-requisitions")
public class PurchaseRequisitionController {
    private final PurchaseRequisitionService purchaseRequisitionService;

    @GetMapping
    public List<PurchaseRequisitionDto> getAllPurchaseRequisitions() {
        return purchaseRequisitionService.getAllPurchaseRequisitions();
    }

    @PostMapping("/filter")
    public PagedResponse<PurchaseRequisitionDto> filterManufacturers(
            @RequestBody PurchaseRequisitionFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PurchaseRequisitionDto> result = purchaseRequisitionService.getPurchaseRequisitions(filter, pageable);
        return new PagedResponse<>(result);
    }

    @GetMapping("/{id}")
    public PurchaseRequisitionDto getPurchaseRequisition(@PathVariable Long id) {
        return purchaseRequisitionService.getPurchaseRequisition(id);
    }

    @PostMapping
    public ResponseEntity<PurchaseRequisitionDto> createPurchaseRequisition(@Valid @RequestBody PurchaseRequisitionCreateRequest request, UriComponentsBuilder uriBuilder) {
        var PurchaseRequisitionDto = purchaseRequisitionService.createPurchaseRequisition(request);
        var uri = uriBuilder.path("/purchase-requisition/{id}").buildAndExpand(PurchaseRequisitionDto.getId()).toUri();
        return ResponseEntity.created(uri).body(PurchaseRequisitionDto);
    }

    @PutMapping("/{id}")
    public PurchaseRequisitionDto updatePurchaseRequisition(
            @PathVariable(name = "id") Long id,
            @RequestBody PurchaseRequisitionUpdateRequest request) {
        return purchaseRequisitionService.updatePurchaseRequisition(id, request);
    }

    @DeleteMapping("/{id}")
    public PurchaseRequisitionDto deletePurchaseRequisition(
            @PathVariable(name = "id") Long id) {
        return purchaseRequisitionService.deletePurchaseRequisition(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkPurchaseRequisition(@RequestBody PurchaseRequisitionBulkUpdateRequest request) {
        var count = purchaseRequisitionService.deleteBulkPurchaseRequisition(request.getPurchaseRequisitionIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    @GetMapping("/{id}/activate")
    public PurchaseRequisitionDto activatePurchaseRequisition(
            @PathVariable(name = "id") Long id) {
        return purchaseRequisitionService.activePurchaseRequisition(id);
    }

    @GetMapping("/{id}/deactivate")
    public PurchaseRequisitionDto deactivateGeneric(
            @PathVariable(name = "id") Long id) {
        return purchaseRequisitionService.deactivatePurchaseRequisition(id);
    }

    //  For  Purchase Requisition item
    @PostMapping("/{id}/filter")
    public PurchaseRequisitionWithItemPageResponse getPurchaseRequisitionWithItemPagination(@PathVariable Long id,
                                                                                            @RequestBody PurchaseRequisitionItemFilter filter,
                                                                                            @RequestParam(defaultValue = "0") int page,
                                                                                            @RequestParam(defaultValue = "10") int size,
                                                                                            @RequestParam(defaultValue = "id") String sortBy,
                                                                                            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return purchaseRequisitionService.getPurchaseRequisitionWithItemPagination(id, pageable, filter);
    }

    @PostMapping("/{id}/add-item")
    public PurchaseRequisitionItemDto addPurchaseCartItem(
            @PathVariable(name = "id") Long id,
            @RequestBody PurchaseRequisitionItemAddRequest request) {
        return purchaseRequisitionService.addPurchaseRequisitionItem(id, request);
    }

    @PostMapping("/{purchaseRequisitionId}/update-item/{purchaseRequisitionItemId}")
    public PurchaseRequisitionItemDto updatePurchaseCartItem(
            @PathVariable(name = "purchaseRequisitionId") Long purchaseRequisitionId,
            @PathVariable(name = "purchaseRequisitionItemId") Long purchaseRequisitionItemId,
            @RequestBody PurchaseRequisitionItemUpdateRequest request) {
        return purchaseRequisitionService.updatePurchaseRequisitionItem(purchaseRequisitionId, purchaseRequisitionItemId, request);
    }

    @DeleteMapping("/{purchaseRequisitionId}/remove-item/{purchaseRequisitionItemId}")
    public PurchaseRequisitionItemDto removePurchaseRequisitionItem(
            @PathVariable(name = "purchaseRequisitionId") Long purchaseRequisitionId, @PathVariable(name = "purchaseRequisitionItemId") Long purchaseRequisitionItemId) {
        return purchaseRequisitionService.removePurchaseRequisitionItem(purchaseRequisitionId, purchaseRequisitionItemId);
    }

    @PostMapping("/{purchaseRequisitionId}/remove-bulk")
    public ResponseEntity<Map<String, Integer>> removeBulkPurchaseRequisitionItem(@PathVariable(name = "purchaseRequisitionId") Long purchaseRequisitionId, @RequestBody PurchaseRequisitionItemBulkRemoveRequest request) {
        var count = purchaseRequisitionService.removeBulkPurchaseRequisitionItem(purchaseRequisitionId, request.getPurchaseRequisitionIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    @ExceptionHandler(DuplicatePurchaseRequisitionException.class)
    public ResponseEntity<Map<String, String>> handleDuplicatePurchaseRequisition(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }

    @ExceptionHandler(PurchaseRequisitionNotFoundException.class)
    public ResponseEntity<Void> handlePurchaseRequisitionNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(RequsitionOnPathNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRequsitionApproverNotFoundException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
//        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(RequisitionUpdateException.class)
    public ResponseEntity<Map<String, String>> handleRequisitionUpdateException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
//        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(PurchaseRequisitionEmptyException.class)
    public ResponseEntity<Map<String, String>> handlePurchaseRequisitionEmptyException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
//        return ResponseEntity.notFound().build();
    }
}
