package com.osudpotro.posmaster.tms.vechile;

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
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    //    @PreAuthorize("hasAuthority('VEHICLE_READ')")
    @GetMapping
    public List<VehicleDto> getAllVehicles() {
        return vehicleService.gerAllVehicles();
    }
    @PostMapping("/filter")
    public PagedResponse<VehicleDto> filterVehicles(
            @RequestBody VehicleFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<VehicleDto> result = vehicleService.filterVehicles(filter, pageable);
        return new PagedResponse<>(result);
    }

    //    @PreAuthorize("hasAuthority('VEHICLE_READ')")
    @GetMapping("/{id}")
    public VehicleDto getVehicle(@PathVariable Long id) {
        return vehicleService.getVehicle(id);
    }

    //    @PreAuthorize("hasAuthority('VEHICLE_CREATE')")
    @PostMapping
    public ResponseEntity<VehicleDto> createVehicle(@Valid @RequestBody VehicleCreateRequest request, UriComponentsBuilder uriBuilder) {
        var customerDto = vehicleService.createVehicle(request);
        var uri = uriBuilder.path("/vehicles/{id}").buildAndExpand(customerDto.getId()).toUri();
        return ResponseEntity.created(uri).body(customerDto);
    }

    //@PreAuthorize("hasAuthority('VEHICLE_UPDATE')")
    @PutMapping("/{id}")
    public VehicleDto updateVehicle(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateVehicleRequest request) {
        return vehicleService.updateVehicle(id, request);
    }

    //@PreAuthorize("hasAuthority('VEHICLE_DELETE')")
    @DeleteMapping("/{id}")
    public VehicleDto deleteVehicle(
            @PathVariable(name = "id") Long id) {
        return vehicleService.deleteVehicle(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkVehicle(@RequestBody VehicleBulkUpdateRequest request) {
        int count = vehicleService.deleteBulkVehicle(request.getVehicleIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    //    @PreAuthorize("hasAuthority('VEHICLE_DELETE')")
    @GetMapping("/{id}/activate")
    public VehicleDto activateVehicle(
            @PathVariable(name = "id") Long id) {
        return vehicleService.activeVehicle(id);
    }

    //    @PreAuthorize("hasAuthority('VEHICLE_DELETE')")
    @GetMapping("/{id}/deactivate")
    public VehicleDto deactivateVehicle(
            @PathVariable(name = "id") Long id) {
        return vehicleService.deactivateVehicle(id);
    }

    @ExceptionHandler(DuplicateVehicleException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateVehicle(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("error", ex.getMessage())
        );
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<Void> handleVehicleNotFound() {
        return ResponseEntity.notFound().build();
    }
}
