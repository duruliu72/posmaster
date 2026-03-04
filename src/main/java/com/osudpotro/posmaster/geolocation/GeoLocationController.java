package com.osudpotro.posmaster.geolocation;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/geo-location")
public class GeoLocationController {
    private final GeoLocationService geoLocationService;
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String searchKey) {
        try {
            var geoLocations = geoLocationService.autocomplete(searchKey);
            return ResponseEntity.ok(geoLocations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", e.getMessage(),
                    "status", "ERROR"
            ));
        }
    }
    @GetMapping("/reverse")
    public ResponseEntity<?> getLocationFromLatLng(@RequestParam double lat,@RequestParam double lon) {
        try {
            var geoLocation = geoLocationService.getLocationFromLatLng(lat,lon);
            return ResponseEntity.ok(geoLocation);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", e.getMessage(),
                    "status", "ERROR"
            ));
        }
    }
}
