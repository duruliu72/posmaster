package com.osudpotro.posmaster.sale;

import com.osudpotro.posmaster.common.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    /**
     * CHECKOUT — Convert SaleCart to Sale
     */
    @PostMapping("/checkout/{saleCartId}")
    public SaleDto checkout(@PathVariable(name = "saleCartId") Long saleCartId,
                            @RequestBody SaleCheckoutRequest request) {
        return saleService.checkoutSaleCart(saleCartId, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }
}