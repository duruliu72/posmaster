package com.osudpotro.posmaster.address.district;

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
@RequestMapping("/districts")
public class DistrictController {
    private final DistrictService districtService;

    //    @PreAuthorize("hasAuthority('BRANCH_READ')")
    @GetMapping
    public List<DistrictDto> getAllEntities() {
        return districtService.getAllEntities();
    }

    @PostMapping("/filter")
    public PagedResponse<DistrictDto> getAllEntities(
            @RequestBody DistrictFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DistrictDto> result = districtService.getAllEntities(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return districtService.importEntities(file);
    }

    @GetMapping("/{id}")
    public DistrictDto getEntity(@PathVariable Long id) {
        return districtService.getEntity(id);
    }

    @PostMapping
    public ResponseEntity<DistrictDto> createEntity(@Valid @RequestBody DistrictCreateRequest request, UriComponentsBuilder uriBuilder) {
        var entityDto = districtService.createEntity(request);
        var uri = uriBuilder.path("/districts/{id}").buildAndExpand(entityDto.getId()).toUri();
        return ResponseEntity.created(uri).body(entityDto);
    }

    @PutMapping("/{id}")
    public DistrictDto updateEntity(
            @PathVariable(name = "id") Long id,
            @RequestBody DistrictUpdateRequest request) {
        return districtService.updateEntity(id, request);
    }

    @DeleteMapping("/{id}")
    public DistrictDto deleteEntity(
            @PathVariable(name = "id") Long id) {
        return districtService.deleteEntity(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkEntity(@RequestBody DistrictBulkUpdateRequest request) {
        var count = districtService.deleteBulkEntity(request.getDistrictIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    @GetMapping("/{id}/activate")
    public DistrictDto activateEntity(
            @PathVariable(name = "id") Long id) {
        return districtService.activateEntity(id);
    }

    @GetMapping("/{id}/deactivate")
    public DistrictDto deactivateEntity(
            @PathVariable(name = "id") Long id) {
        return districtService.deactivateEntity(id);
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