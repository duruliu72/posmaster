package com.osudpotro.posmaster.purchase.purchasecart;

import com.osudpotro.posmaster.common.PagedResponse;
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
@RequestMapping("/purchase-cart")
public class PurchaseCartController {
    private final PurchaseCartService purchaseCartService;

    @GetMapping
    public List<PurchaseCartDto> getAllPurchaseCarts() {
        return purchaseCartService.getAllPurchaseCarts();
    }

    @PostMapping("/filter")
    public PagedResponse<PurchaseCartDto> filterPurchaseCarts(
            @RequestBody PurchaseCartFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PurchaseCartDto> result = purchaseCartService.getPurchaseCarts(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/{id}/filter")
    public PurchaseCartWithItemPageResponse getPurchaseCartWithItemPagination(@PathVariable Long id,
                                                                              @RequestBody PurchaseCartItemFilter filter,
                                                                              @RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "10") int size,
                                                                              @RequestParam(defaultValue = "id") String sortBy,
                                                                              @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return purchaseCartService.getPurchaseCartWithItemPagination(id, pageable, filter);
    }

    @PostMapping
    public ResponseEntity<PurchaseCartDto> createPurchaseCart(@Valid @RequestBody PurchaseCartCreateRequest request, UriComponentsBuilder uriBuilder) {
        var genericDto = purchaseCartService.createPurchaseCart(request);
        var uri = uriBuilder.path("/purchase-cart/{id}").buildAndExpand(genericDto.getId()).toUri();
        return ResponseEntity.created(uri).body(genericDto);
    }

    @PutMapping("/{id}")
    public PurchaseCartDto updatePurchaseCart(
            @PathVariable(name = "id") Long id,
            @RequestBody PurchaseCartUpdateRequest request) {
        return purchaseCartService.updatePurchaseCart(id, request);
    }

    @PostMapping("/{id}/add-item")
    public PurchaseCartItemDto addPurchaseCartItem(
            @PathVariable(name = "id") Long id,
            @RequestBody PurchaseCartItemAddRequest request) {
        return purchaseCartService.addPurchaseCartItem(id, request);
    }

    @PostMapping("/{purchaseCartId}/update-item/{purchaseCartItemId}")
    public PurchaseCartItemDto updatePurchaseCartItem(
            @PathVariable(name = "purchaseCartId") Long purchaseCartId,
            @PathVariable(name = "purchaseCartItemId") Long purchaseCartItemId,
            @RequestBody PurchaseCartItemUpdateRequest request) {
        return purchaseCartService.updatePurchaseCartItem(purchaseCartId, purchaseCartItemId, request);
    }

    @GetMapping("/{id}")
    public PurchaseCartDto getPurchaseCart(@PathVariable Long id) {
        return purchaseCartService.getPurchaseCart(id);
    }

    @DeleteMapping("/{id}")
    public PurchaseCartDto deletePurchaseCart(
            @PathVariable(name = "id") Long id) {
        return purchaseCartService.deletePurchaseCart(id);
    }

    @DeleteMapping("/{purchaseCartId}/remove-item/{purchaseCartItemId}")
    public PurchaseCartItemDto deletePurchaseCartItem(
            @PathVariable(name = "purchaseCartId") Long purchaseCartId, @PathVariable(name = "purchaseCartItemId") Long purchaseCartItemId) {
        return purchaseCartService.removePurchaseCartItem(purchaseCartId, purchaseCartItemId);
    }

    @PostMapping("/{purchaseCartId}/remove-bulk")
    public ResponseEntity<Map<String, Integer>> removeBulkPurchaseCartItem(@PathVariable(name = "purchaseCartId") Long purchaseCartId, @RequestBody PurchaseCartItemBulkRemoveRequest request) {
        var count = purchaseCartService.removeBulkPurchaseCartItem(purchaseCartId, request.getPurchaseCartItemIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }
    @ExceptionHandler(PurchaseCartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePurchaseCartNotFound(Exception ex) {
        return ResponseEntity.badRequest().build();
    }
    @ExceptionHandler(PurchaseCartItemDuplicateException.class)
    public ResponseEntity<Map<String, String>> handleDuplicatePurchaseCartItem(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("message", "Item is already exist.")
        );
    }
}
