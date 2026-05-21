package com.osudpotro.posmaster.sale;

import com.osudpotro.posmaster.common.EntityNotFoundException;
import com.osudpotro.posmaster.common.PagedResponse;
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
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    // Add these to SaleController.java

    @GetMapping
    public List<SaleDto> getAllSales() {
        return saleService.getAllSales();
    }

    @PostMapping("/filter")
    public PagedResponse<SaleDto> filterSales(
            @RequestBody SaleFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SaleDto> result = saleService.filterSales(filter, pageable);
        return new PagedResponse<>(result);
    }

    @GetMapping("/{id}")
    public SaleDto getSale(@PathVariable Long id) {
        return saleService.getSale(id);
    }


    /**
     * CHECKOUT — Convert SaleCart to Sale
     */
    @PostMapping("/checkout/{saleCartId}")
    public SaleDto checkout(@PathVariable(name = "saleCartId") Long saleCartId,
                            @RequestBody SaleCheckoutRequest request) {
        return saleService.checkoutSaleCart(saleCartId, request);
    }

    @PutMapping("/{id}/status")
    public SaleDto updateSaleStatus(@PathVariable Long id, @RequestBody SaleStatusUpdateRequest request) {
        return saleService.updateSaleStatus(id, request);
    }

    @PutMapping("/{id}/payment-status")
    public SaleDto updatePaymentStatus(@PathVariable Long id, @RequestBody SalePaymentStatusUpdateRequest request) {
        return saleService.updatePaymentStatus(id, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }
}