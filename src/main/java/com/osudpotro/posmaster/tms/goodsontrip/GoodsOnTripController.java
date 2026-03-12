package com.osudpotro.posmaster.tms.goodsontrip;

import com.osudpotro.posmaster.tms.vehicletrip.DuplicateVehicleTripException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @ExceptionHandler(GoodsOnTripAlreadyDeliveredException.class)
    public ResponseEntity<Map<String, String>> handleGoodsOnTripAlreadyDelivered(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("error", ex.getMessage())
        );
    }
    @ExceptionHandler(GoodsOnTripNotFoundException.class)
    public ResponseEntity<Void> handleGoodsOnTripNotFound() {
        return ResponseEntity.notFound().build();
    }
}
