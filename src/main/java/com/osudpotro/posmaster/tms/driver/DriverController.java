package com.osudpotro.posmaster.tms.driver;


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
@RequestMapping("/drivers")
public class DriverController {
    private final DriverService driverService;
    //    @PreAuthorize("hasAuthority('DRIVER_READ')")
    @GetMapping
    public List<DriverDto> getAllVehicleDrivers() {
        return driverService.gerAllVehicleDrivers();
    }
    @PostMapping("/filter")
    public PagedResponse<DriverDto> searchVehicleDrivers(
            @RequestBody DriverFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DriverDto> result = driverService.getVehicleDrivers(filter, pageable);
        return new PagedResponse<>(result);
    }

    //    @PreAuthorize("hasAuthority('DRIVER_READ')")
    @GetMapping("/{id}")
    public DriverDto getVehicleDriver(@PathVariable Long id) {
        return driverService.getVehicleDriver(id);
    }

    //    @PreAuthorize("hasAuthority('DRIVER_CREATE')")
    @PostMapping
    public ResponseEntity<DriverDto> createVehicleDriver(@Valid @RequestBody DriverCreateRequest request, UriComponentsBuilder uriBuilder) {
        var customerDto = driverService.registerDriver(request);
        var uri = uriBuilder.path("/customers/{id}").buildAndExpand(customerDto.getId()).toUri();
        return ResponseEntity.created(uri).body(customerDto);
    }

    //@PreAuthorize("hasAuthority('DRIVER_UPDATE')")
    @PutMapping("/{id}")
    public DriverDto updateDriver(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateDriverRequest request) {
        return driverService.updateDriver(id, request);
    }

    //@PreAuthorize("hasAuthority('DRIVER_DELETE')")
    @DeleteMapping("/{id}")
    public DriverDto deleteDriver(
            @PathVariable(name = "id") Long id) {
        return driverService.deleteDriver(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkVehicle(@RequestBody DriverBulkUpdateRequest request) {
        int count = driverService.deleteBulkVehicle(request.getDriverIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    //    @PreAuthorize("hasAuthority('DRIVER_DELETE')")
    @GetMapping("/{id}/activate")
    public DriverDto activeDriver(
            @PathVariable(name = "id") Long id) {
        return driverService.activeDriver(id);
    }

    //    @PreAuthorize("hasAuthority('VEHICLE_DRIVER_DELETE')")
    @GetMapping("/{id}/deactivate")
    public DriverDto deactivateDriver(
            @PathVariable(name = "id") Long id) {
        return driverService.deactivateDriver(id);
    }

    @ExceptionHandler(DuplicateDriverException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateDriver(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("error", ex.getMessage())
        );
    }

    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<Void> handleDriverNotFound() {
        return ResponseEntity.notFound().build();
    }
}
