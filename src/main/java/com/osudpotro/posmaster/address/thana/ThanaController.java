package com.osudpotro.posmaster.address.thana;

import com.osudpotro.posmaster.address.district.*;
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
@RequestMapping("/thanas")
public class ThanaController {
    private final ThanaService thanaService;
    //    @PreAuthorize("hasAuthority('BRANCH_READ')")
    @GetMapping
    public List<ThanaDto> getAllEntities() {
        return thanaService.getAllEntities();
    }

    @PostMapping("/filter")
    public PagedResponse<ThanaDto> getAllEntities(
            @RequestBody ThanaFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ThanaDto> result = thanaService.getAllEntities(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return thanaService.importEntities(file);
    }

    @GetMapping("/{id}")
    public ThanaDto getEntity(@PathVariable Long id) {
        return thanaService.getEntity(id);
    }

    @PostMapping
    public ResponseEntity<ThanaDto> createEntity(@Valid @RequestBody ThanaCreateRequest request, UriComponentsBuilder uriBuilder) {
        var entityDto = thanaService.createEntity(request);
        var uri = uriBuilder.path("/districts/{id}").buildAndExpand(entityDto.getId()).toUri();
        return ResponseEntity.created(uri).body(entityDto);
    }

    @PutMapping("/{id}")
    public ThanaDto updateEntity(
            @PathVariable(name = "id") Long id,
            @RequestBody ThanaUpdateRequest request) {
        return thanaService.updateEntity(id, request);
    }

    @DeleteMapping("/{id}")
    public ThanaDto deleteEntity(
            @PathVariable(name = "id") Long id) {
        return thanaService.deleteEntity(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkEntity(@RequestBody DistrictBulkUpdateRequest request) {
        var count = thanaService.deleteBulkEntity(request.getDistrictIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    @GetMapping("/{id}/activate")
    public ThanaDto activateEntity(
            @PathVariable(name = "id") Long id) {
        return thanaService.activateEntity(id);
    }

    @GetMapping("/{id}/deactivate")
    public ThanaDto deactivateEntity(
            @PathVariable(name = "id") Long id) {
        return thanaService.deactivateEntity(id);
    }


    @ExceptionHandler(DuplicateDistrictException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateDistrictException(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }

    @ExceptionHandler(DistrictNotFoundException.class)
    public ResponseEntity<Void> handleDistrictNotFound() {
        return ResponseEntity.notFound().build();
    }

}