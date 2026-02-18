package com.osudpotro.posmaster.resource;

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
@RequestMapping("/resources")
public class ResourceController {
    private final ResourceService resourceService;

    //    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    @GetMapping
    public List<ResourceDto> getAllResources() {
        return resourceService.gerAllResources();
    }

    @PostMapping("/filter")
    public PagedResponse<ResourceDto> filterResources(
            @RequestBody ResourceFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ResourceDto> result = resourceService.filterResources(filter, pageable);
        return new PagedResponse<>(result);
    }
    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return resourceService.importResources(file);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    @GetMapping("/{id}")
    public ResourceDto getResource(@PathVariable Long id) {
        return resourceService.getResource(id);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_CREATE')")
    @PostMapping
    public ResponseEntity<ResourceDto> createResource(@Valid @RequestBody ResourceCreateRequest request, UriComponentsBuilder uriBuilder) {
        var customerDto = resourceService.createResource(request);
        var uri = uriBuilder.path("/ui-resources/{id}").buildAndExpand(customerDto.getId()).toUri();
        return ResponseEntity.created(uri).body(customerDto);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_UPDATE')")
    @PutMapping("/{id}")
    public ResourceDto updateResource(
            @PathVariable(name = "id") Long id,
            @RequestBody ResourceUpdateRequest request) {
        return resourceService.updateResource(id, request);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_DELETE')")
    @DeleteMapping("/{id}")
    public ResourceDto deleteResource(
            @PathVariable(name = "id") Long id) {
        return resourceService.deleteResource(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkResource(@RequestBody ResourceBulkUpdateRequest request) {
        int count = resourceService.deleteBulkResource(request.getResourceIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_DELETE')")
    @GetMapping("/{id}/activate")
    public ResourceDto activateResource(
            @PathVariable(name = "id") Long id) {
        return resourceService.activeResource(id);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_DELETE')")
    @GetMapping("/{id}/deactivate")
    public ResourceDto deactivateResource(
            @PathVariable(name = "id") Long id) {
        return resourceService.deactivateResource(id);
    }
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateUiResource(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("error", ex.getMessage())
        );
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Void> handleResourceNotFound() {
        return ResponseEntity.notFound().build();
    }
}
