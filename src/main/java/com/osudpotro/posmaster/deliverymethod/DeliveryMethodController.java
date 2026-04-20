package com.osudpotro.posmaster.deliverymethod;

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
@RequestMapping("/delivery-methods")
public class DeliveryMethodController {
    private final DeliveryMethodService dvmService;

    //    @PreAuthorize("hasAuthority('BRANCH_READ')")
    @GetMapping
    public List<DeliveryMethodDto> getAllEntities() {
        return dvmService.getAllEntities();
    }

    @PostMapping("/filter")
    public PagedResponse<DeliveryMethodDto> getAllEntities(
            @RequestBody DeliveryMethodFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DeliveryMethodDto> result = dvmService.getAllEntities(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return dvmService.importEntities(file);
    }

    @GetMapping("/{id}")
    public DeliveryMethodDto getEntity(@PathVariable Long id) {
        return dvmService.getEntity(id);
    }

    @PostMapping
    public ResponseEntity<DeliveryMethodDto> createEntity(@Valid @RequestBody DeliveryMethodCreateRequest request, UriComponentsBuilder uriBuilder) {
        var entityDto = dvmService.createEntity(request);
        var uri = uriBuilder.path("/delivery-methods/{id}").buildAndExpand(entityDto.getId()).toUri();
        return ResponseEntity.created(uri).body(entityDto);
    }

    @PutMapping("/{id}")
    public DeliveryMethodDto updateEntity(
            @PathVariable(name = "id") Long id,
            @RequestBody DeliveryMethodUpdateRequest request) {
        return dvmService.updateEntity(id, request);
    }

    @DeleteMapping("/{id}")
    public DeliveryMethodDto deleteEntity(
            @PathVariable(name = "id") Long id) {
        return dvmService.deleteEntity(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkEntity(@RequestBody DeliveryMethodBulkUpdateRequest request) {
        var count = dvmService.deleteBulkEntity(request.getDeliveryMethodIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    @GetMapping("/{id}/activate")
    public DeliveryMethodDto activateEntity(
            @PathVariable(name = "id") Long id) {
        return dvmService.activateEntity(id);
    }

    @GetMapping("/{id}/deactivate")
    public DeliveryMethodDto deactivateEntity(
            @PathVariable(name = "id") Long id) {
        return dvmService.deactivateEntity(id);
    }


    @ExceptionHandler(DuplicateDeliveryMethodException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateAreaException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }
    @ExceptionHandler(DeliveryMethodNotFoundException.class)
    public ResponseEntity<Void> handleAreaNotFound() {
        return ResponseEntity.notFound().build();
    }

}