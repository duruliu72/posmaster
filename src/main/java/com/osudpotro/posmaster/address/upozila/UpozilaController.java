
package com.osudpotro.posmaster.address.upozila;
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
@RequestMapping("/upozilas")
public class UpozilaController {
    private final UpozilaService upozilaService;
    //    @PreAuthorize("hasAuthority('BRANCH_READ')")
    @GetMapping
    public List<UpozilaDto> getAllEntities() {
        return upozilaService.getAllEntities();
    }

    @PostMapping("/filter")
    public PagedResponse<UpozilaDto> getAllEntities(
            @RequestBody UpozilaFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UpozilaDto> result = upozilaService.getAllEntities(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return upozilaService.importEntities(file);
    }

    @GetMapping("/{id}")
    public UpozilaDto getEntity(@PathVariable Long id) {
        return upozilaService.getEntity(id);
    }

    @PostMapping
    public ResponseEntity<UpozilaDto> createEntity(@Valid @RequestBody UpozilaCreateRequest request, UriComponentsBuilder uriBuilder) {
        var entityDto = upozilaService.createEntity(request);
        var uri = uriBuilder.path("/upozilas/{id}").buildAndExpand(entityDto.getId()).toUri();
        return ResponseEntity.created(uri).body(entityDto);
    }

    @PutMapping("/{id}")
    public UpozilaDto updateEntity(
            @PathVariable(name = "id") Long id,
            @RequestBody UpozilaUpdateRequest request) {
        return upozilaService.updateEntity(id, request);
    }

    @DeleteMapping("/{id}")
    public UpozilaDto deleteEntity(
            @PathVariable(name = "id") Long id) {
        return upozilaService.deleteEntity(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkEntity(@RequestBody UpozilaBulkUpdateRequest request) {
        var count = upozilaService.deleteBulkEntity(request.getUpozilaIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    @GetMapping("/{id}/activate")
    public UpozilaDto activateEntity(
            @PathVariable(name = "id") Long id) {
        return upozilaService.activateEntity(id);
    }

    @GetMapping("/{id}/deactivate")
    public UpozilaDto deactivateEntity(
            @PathVariable(name = "id") Long id) {
        return upozilaService.deactivateEntity(id);
    }


    @ExceptionHandler(DuplicateUpozilaException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateUpozilaException(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }

    @ExceptionHandler(UpozilaNotFoundException.class)
    public ResponseEntity<Void> handleUpozilaNotFound() {
        return ResponseEntity.notFound().build();
    }

}