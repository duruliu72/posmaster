package com.osudpotro.posmaster.tms.vehicletrip;

import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.tms.vechile.*;
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
@RequestMapping("/vehicle-trips")
public class VehicleTripController {
    private final VehicleTripService vehicleTripService;
    //    @PreAuthorize("hasAuthority('VEHICLE_READ')")
    @GetMapping
    public List<VehicleTripDto> getAllVehicleTrips() {
        return vehicleTripService.getAllVehicleTrips();
    }
    @PostMapping("/filter")
    public PagedResponse<VehicleTripDto> filterVehicleTrips(
            @RequestBody VehicleTripFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<VehicleTripDto> result = vehicleTripService.filterVehicleTrips(filter, pageable);
        return new PagedResponse<>(result);
    }

    //    @PreAuthorize("hasAuthority('VEHICLE_READ')")
    @GetMapping("/{id}")
    public VehicleTripDto getVehicleTrip(@PathVariable Long id) {
        return vehicleTripService.getVehicleTrip(id);
    }

    //    @PreAuthorize("hasAuthority('VEHICLE_CREATE')")
    @PostMapping
    public ResponseEntity<VehicleTripDto> createVehicleTrip(@Valid @RequestBody VehicleTripCreateRequest request, UriComponentsBuilder uriBuilder) {
        var customerDto = vehicleTripService.createVehicleTrip(request);
        var uri = uriBuilder.path("/vehicle-trips/{id}").buildAndExpand(customerDto.getId()).toUri();
        return ResponseEntity.created(uri).body(customerDto);
    }

    //@PreAuthorize("hasAuthority('VEHICLE_UPDATE')")
    @PutMapping("/{id}")
    public VehicleTripDto updateVehicle(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateVehicleTripRequest request) {
        return vehicleTripService.updateVehicleTrip(id, request);
    }

    //@PreAuthorize("hasAuthority('VEHICLE_DELETE')")
    @DeleteMapping("/{id}")
    public VehicleTripDto deleteVehicleTrip(
            @PathVariable(name = "id") Long id) {
        return vehicleTripService.deleteVehicleTrip(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkVehicleTrip(@RequestBody VehicleTripBulkUpdateRequest request) {
        int count = vehicleTripService.deleteBulkVehicleTrip(request.getVehicleTripIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    //    @PreAuthorize("hasAuthority('VEHICLE_DELETE')")
    @GetMapping("/{id}/activate")
    public VehicleTripDto activateVehicleTrip(
            @PathVariable(name = "id") Long id) {
        return vehicleTripService.activeVehicleTrip(id);
    }

    //    @PreAuthorize("hasAuthority('VEHICLE_DELETE')")
    @GetMapping("/{id}/deactivate")
    public VehicleTripDto deactivateVehicleTrip(
            @PathVariable(name = "id") Long id) {
        return vehicleTripService.deactivateVehicleTrip(id);
    }

    @ExceptionHandler(DuplicateVehicleTripException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateVehicleTrip(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("error", ex.getMessage())
        );
    }

    @ExceptionHandler(VehicleTripNotFoundException.class)
    public ResponseEntity<Void> handleVehicleTripNotFound() {
        return ResponseEntity.notFound().build();
    }
}
