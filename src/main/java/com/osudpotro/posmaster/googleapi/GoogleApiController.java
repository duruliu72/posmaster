package com.osudpotro.posmaster.googleapi;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/google-api/places")
public class GoogleApiController {
    private final GoogleApiService googleApiService;
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String searchKey) {
        try {
            var predictions = googleApiService.autocomplete(searchKey);
            return ResponseEntity.ok(predictions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", e.getMessage(),
                    "status", "ERROR"
            ));
        }
    }
}
