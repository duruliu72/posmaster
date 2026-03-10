package com.osudpotro.posmaster.tms.goodsontrip;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/goods-on-trips")
public class GoodsOnTripController {
    private final GoodsOnTripService goodsOnTripService;
    @PutMapping("/{id}")
    public GoodsOnTripDto updateGoodsTrip(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody UpdateGoodsOnTripRequest request) {
        return goodsOnTripService.updateGoodsTrip(id, request);
    }
    @ExceptionHandler(GoodsOnTripNotFoundException.class)
    public ResponseEntity<Void> handleGoodsOnTripNotFound() {
        return ResponseEntity.notFound().build();
    }
}
