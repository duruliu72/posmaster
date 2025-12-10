package com.osudpotro.posmaster.resource.ui;

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
@RequestMapping("/ui-resources")
public class UiResourceController {
    private final UiResourceService uiResourceService;

    //    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    @GetMapping
    public List<UiResourceDto> getAllUiResources() {
        return uiResourceService.gerAllUiResources();
    }

    @PostMapping("/filter")
    public PagedResponse<UiResourceDto> searchUiResources(
            @RequestBody UiResourceFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UiResourceDto> result = uiResourceService.getUiResources(filter, pageable);
        return new PagedResponse<>(result);
    }
    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return uiResourceService.importUiResources(file);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    @GetMapping("/{id}")
    public UiResourceDto getUiResource(@PathVariable Long id) {
        return uiResourceService.getUiResource(id);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_CREATE')")
    @PostMapping
    public ResponseEntity<UiResourceDto> createUiResource(@Valid @RequestBody UiResourceCreateRequest request, UriComponentsBuilder uriBuilder) {
        var customerDto = uiResourceService.CreateUiResource(request);
        var uri = uriBuilder.path("/ui-resources/{id}").buildAndExpand(customerDto.getId()).toUri();
        return ResponseEntity.created(uri).body(customerDto);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_UPDATE')")
    @PutMapping("/{id}")
    public UiResourceDto updateUiResource(
            @PathVariable(name = "id") Long id,
            @RequestBody UiResourceUpdateRequest request) {
        return uiResourceService.updateUiResource(id, request);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_DELETE')")
    @DeleteMapping("/{id}")
    public UiResourceDto deleteUiResource(
            @PathVariable(name = "id") Long id) {
        return uiResourceService.deleteUiResource(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkUiResource(@RequestBody UiResourceBulkUpdateRequest request) {
        int count = uiResourceService.deleteBulkUiResource(request.getUiResourceIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_DELETE')")
    @GetMapping("/{id}/activate")
    public UiResourceDto activateUiResource(
            @PathVariable(name = "id") Long id) {
        return uiResourceService.activeUiResource(id);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_DELETE')")
    @GetMapping("/{id}/deactivate")
    public UiResourceDto deactivateUiResource(
            @PathVariable(name = "id") Long id) {
        return uiResourceService.deactivateUiResource(id);
    }
    @ExceptionHandler(DuplicateUiResourceException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateUiResource(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("error", ex.getMessage())
        );
    }
    @ExceptionHandler(UiResourceNotFoundException.class)
    public ResponseEntity<Void> handleUiResourceNotFound() {
        return ResponseEntity.notFound().build();
    }
}
