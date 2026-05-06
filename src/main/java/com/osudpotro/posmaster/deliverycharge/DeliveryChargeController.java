package com.osudpotro.posmaster.deliverycharge;

import com.osudpotro.posmaster.common.PagedResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/delivery-charges")
public class DeliveryChargeController {
    private final DeliveryChargeService dvcService;
    //    @PreAuthorize("hasAuthority('BRANCH_READ')")
    @GetMapping
    public List<DeliveryChargeDto> getAllEntities() {
        return dvcService.getAllEntities();
    }

    @PostMapping("/filter")
    public PagedResponse<DeliveryChargeDto> getAllEntities(
            @RequestBody DeliveryChargeFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DeliveryChargeDto> result = dvcService.getAllEntities(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return dvcService.importEntities(file);
    }

    @GetMapping("/{id}")
    public DeliveryChargeDto getEntity(@PathVariable Long id) {
        return dvcService.getEntity(id);
    }

    @PostMapping
    public ResponseEntity<DeliveryChargeDto> createEntity(@Valid @RequestBody DeliveryChargeCreateRequest request, UriComponentsBuilder uriBuilder) {
        var entityDto = dvcService.createEntity(request);
        var uri = uriBuilder.path("/delivery-charges/{id}").buildAndExpand(entityDto.getId()).toUri();
        return ResponseEntity.created(uri).body(entityDto);
    }

    @PutMapping("/{id}")
    public DeliveryChargeDto updateEntity(
            @PathVariable(name = "id") Long id,
            @RequestBody DeliveryChargeUpdateRequest request) {
        return dvcService.updateEntity(id, request);
    }

    @DeleteMapping("/{id}")
    public DeliveryChargeDto deleteEntity(
            @PathVariable(name = "id") Long id) {
        return dvcService.deleteEntity(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkEntity(@RequestBody DeliveryChargeBulkUpdateRequest request) {
        var count = dvcService.deleteBulkEntity(request.getDeliveryChargeIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    @GetMapping("/{id}/activate")
    public DeliveryChargeDto activateEntity(
            @PathVariable(name = "id") Long id) {
        return dvcService.activateEntity(id);
    }

    @GetMapping("/{id}/deactivate")
    public DeliveryChargeDto deactivateEntity(
            @PathVariable(name = "id") Long id) {
        return dvcService.deactivateEntity(id);
    }


    @ExceptionHandler(DuplicateDeliveryChargeException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateDeliveryCharge(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }
    @ExceptionHandler(DeliveryChargeNotFoundException.class)
    public ResponseEntity<Void> handleDeliveryChargeNotFound() {
        return ResponseEntity.notFound().build();
    }

}