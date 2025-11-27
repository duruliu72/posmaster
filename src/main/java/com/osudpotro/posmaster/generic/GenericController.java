package com.osudpotro.posmaster.generic;

import com.osudpotro.posmaster.branch.BranchBulkUpdateRequest;
import com.osudpotro.posmaster.branch.BranchDto;
import com.osudpotro.posmaster.branch.BranchFilter;
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
@RequestMapping("/generics")
public class GenericController {
    private final GenericService genericService;

    @GetMapping
    public List<GenericDto> getAllGenerics() {
        return genericService.gerAllGenerics();
    }

    @PostMapping("/filter")
    public PagedResponse<GenericDto> filterGenerics(
            @RequestBody GenericFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<GenericDto> result = genericService.getGenerics(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return genericService.importGeneric(file);
    }

    @GetMapping("/{id}")
    public GenericDto getGeneric(@PathVariable Long id) {
        return genericService.getGeneric(id);
    }

    @PostMapping
    public ResponseEntity<GenericDto> createGeneric(@Valid @RequestBody GenericCreateRequest request, UriComponentsBuilder uriBuilder) {
        var genericDto = genericService.createGeneric(request);
        var uri = uriBuilder.path("/generics/{id}").buildAndExpand(genericDto.getId()).toUri();
        return ResponseEntity.created(uri).body(genericDto);
    }

    @PutMapping("/{id}")
    public GenericDto updateGeneric(
            @PathVariable(name = "id") Long id,
            @RequestBody GenericUpdateRequest request) {
        return genericService.updateGeneric(id, request);
    }

    @DeleteMapping("/{id}")
    public GenericDto deleteGeneric(
            @PathVariable(name = "id") Long id) {
        return genericService.deleteGeneric(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkGeneric(@RequestBody GenericBulkUpdateRequest request) {
        var count = genericService.deleteBulkGeneric(request.getGenericIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    @GetMapping("/{id}/activate")
    public GenericDto activateGeneric(
            @PathVariable(name = "id") Long id) {
        return genericService.activeGeneric(id);
    }

    @GetMapping("/{id}/deactivate")
    public GenericDto deactivateGeneric(
            @PathVariable(name = "id") Long id) {
        return genericService.deactivateGeneric(id);
    }

    @ExceptionHandler(DuplicateGenericException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateGeneric(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }

    @ExceptionHandler(GenericNotFoundException.class)
    public ResponseEntity<Void> handleGenericNotFound() {
        return ResponseEntity.notFound().build();
    }
}