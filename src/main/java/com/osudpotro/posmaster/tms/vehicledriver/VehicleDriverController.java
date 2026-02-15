package com.osudpotro.posmaster.tms.vehicledriver;


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
@RequestMapping("/vehicle-drivers")
public class VehicleDriverController {
    private final VehicleDriverService vehicleDriverService;
    //    @PreAuthorize("hasAuthority('VEHICLE_DRIVER_READ')")
    @GetMapping
    public List<VehicleDriverDto> getAllVehicleDrivers() {
        return vehicleDriverService.gerAllVehicleDrivers();
    }
    @PostMapping("/filter")
    public PagedResponse<VehicleDriverDto> searchVehicleDrivers(
            @RequestBody VehicleDriverFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<VehicleDriverDto> result = vehicleDriverService.getVehicleDrivers(filter, pageable);
        return new PagedResponse<>(result);
    }

    //    @PreAuthorize("hasAuthority('VEHICLE_DRIVER_READ')")
    @GetMapping("/{id}")
    public VehicleDriverDto getVehicleDriver(@PathVariable Long id) {
        return vehicleDriverService.getVehicleDriver(id);
    }

    //    @PreAuthorize("hasAuthority('VEHICLE_DRIVER_CREATE')")
    @PostMapping
    public ResponseEntity<VehicleDriverDto> createVehicleDriver(@Valid @RequestBody VehicleDriverCreateRequest request, UriComponentsBuilder uriBuilder) {
        var customerDto = vehicleDriverService.registerVehicleDriver(request);
        var uri = uriBuilder.path("/customers/{id}").buildAndExpand(customerDto.getId()).toUri();
        return ResponseEntity.created(uri).body(customerDto);
    }

    //@PreAuthorize("hasAuthority('VEHICLE_DRIVER_UPDATE')")
    @PutMapping("/{id}")
    public VehicleDriverDto updateVehicleDriver(
            @PathVariable(name = "id") Long id,
            @RequestBody VehicleDriverUpdateRequest request) {
        return vehicleDriverService.updateVehicleDriver(id, request);
    }

    //@PreAuthorize("hasAuthority('VEHICLE_DRIVER_DELETE')")
    @DeleteMapping("/{id}")
    public VehicleDriverDto deleteVehicleDriver(
            @PathVariable(name = "id") Long id) {
        return vehicleDriverService.deleteVehicleDriver(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkVehicleDriver(@RequestBody VehicleDriverBulkUpdateRequest request) {
        int count = vehicleDriverService.deleteBulkVehicleDriver(request.getVehicleDriverIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    //    @PreAuthorize("hasAuthority('VEHICLE_DRIVER_DELETE')")
    @GetMapping("/{id}/activate")
    public VehicleDriverDto activateVehicleDriver(
            @PathVariable(name = "id") Long id) {
        return vehicleDriverService.activeVehicleDriver(id);
    }

    //    @PreAuthorize("hasAuthority('VEHICLE_DRIVER_DELETE')")
    @GetMapping("/{id}/deactivate")
    public VehicleDriverDto deactivateVehicleDriver(
            @PathVariable(name = "id") Long id) {
        return vehicleDriverService.deactivateVehicleDriver(id);
    }

    @ExceptionHandler(DuplicateVehicleDriverException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateVehicleDriver(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("error", ex.getMessage())
        );
    }

    @ExceptionHandler(VehicleDriverNotFoundException.class)
    public ResponseEntity<Void> handleVehicleDriverNotFound() {
        return ResponseEntity.notFound().build();
    }
}
