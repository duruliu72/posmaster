package com.osudpotro.posmaster.address.division;

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
@RequestMapping("/divisions")
public class DivisionController {
    private final DivisionService divisionService;

    //    @PreAuthorize("hasAuthority('BRANCH_READ')")
    @GetMapping
    public List<DivisionDto> getAllEntities() {
        return divisionService.getAllEntities();
    }

    @PostMapping("/filter")
    public PagedResponse<DivisionDto> getAllEntities(
            @RequestBody DivisionFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DivisionDto> result = divisionService.getAllEntities(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return divisionService.importEntities(file);
    }

    @GetMapping("/{id}")
    public DivisionDto getEntity(@PathVariable Long id) {
        return divisionService.getEntity(id);
    }

    @PostMapping
    public ResponseEntity<DivisionDto> createEntity(@Valid @RequestBody DivisionCreateRequest request, UriComponentsBuilder uriBuilder) {
        var entityDto = divisionService.createEntity(request);
        var uri = uriBuilder.path("/divisions/{id}").buildAndExpand(entityDto.getId()).toUri();
        return ResponseEntity.created(uri).body(entityDto);
    }

    @PutMapping("/{id}")
    public DivisionDto updateEntity(
            @PathVariable(name = "id") Long id,
            @RequestBody DivisionUpdateRequest request) {
        return divisionService.updateEntity(id, request);
    }

    @DeleteMapping("/{id}")
    public DivisionDto deleteEntity(
            @PathVariable(name = "id") Long id) {
        return divisionService.deleteEntity(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkEntity(@RequestBody DivisionBulkUpdateRequest request) {
        var count = divisionService.deleteBulkEntity(request.getDivisionIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    @GetMapping("/{id}/activate")
    public DivisionDto activateEntity(
            @PathVariable(name = "id") Long id) {
        return divisionService.activateEntity(id);
    }

    @GetMapping("/{id}/deactivate")
    public DivisionDto deactivateEntity(
            @PathVariable(name = "id") Long id) {
        return divisionService.deactivateEntity(id);
    }


    @ExceptionHandler(DuplicateDivisionException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateDivisionException(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }

    @ExceptionHandler(DivisionNotFoundException.class)
    public ResponseEntity<Void> handleDivisionNotFound() {
        return ResponseEntity.notFound().build();
    }

}