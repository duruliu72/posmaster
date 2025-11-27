package com.osudpotro.posmaster.varianttype;

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
@RequestMapping("/variant-types")
public class VariantTypeController {
    private final VariantTypeService variantTypeService;
    @GetMapping
    public List<VariantTypeDto> getAllVariantTypes(){
        return variantTypeService.getAllVariantTypes();
    }
    @PostMapping("/filter")
    public PagedResponse<VariantTypeDto> filterVariantTypes(
            @RequestBody VariantTypeFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<VariantTypeDto> result = variantTypeService.getVariantTypes(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return variantTypeService.importVariantType(file);
    }
    @GetMapping("/{id}")
    public VariantTypeDto getVariantType(@PathVariable Long id) {
        return variantTypeService.getVariantType(id);
    }
    @PostMapping
    public ResponseEntity<VariantTypeDto> createVariantType(@Valid @RequestBody VariantTypeCreateRequest request, UriComponentsBuilder uriBuilder){
        var variantTypeDto = variantTypeService.createVariantType(request);
        var uri=uriBuilder.path("/variant-types/{id}").buildAndExpand(variantTypeDto.getId()).toUri();
        return  ResponseEntity.created(uri).body(variantTypeDto);
    }
    @PutMapping("/{id}")
    public VariantTypeDto updateVariantType(
            @PathVariable(name = "id") Long id,
            @RequestBody VariantTypeUpdateRequest request) {
        return variantTypeService.updateVariantType(id, request);
    }
    @DeleteMapping("/{id}")
    public VariantTypeDto deleteVariantType(
            @PathVariable(name = "id") Long id) {
        return variantTypeService.deleteVariantType(id);
    }
    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkVariantType(@RequestBody VariantTypeBulkUpdateRequest request) {
        var count = variantTypeService.deleteBulkVariantType(request.getVariantTypeIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }
    @GetMapping("/{id}/activate")
    public VariantTypeDto activateVariantType(
            @PathVariable(name = "id") Long id) {
        return variantTypeService.activeVariantType(id);
    }

    @GetMapping("/{id}/deactivate")
    public VariantTypeDto deActivateVariantType(
            @PathVariable(name = "id") Long id) {
        return variantTypeService.deActiveVariantType(id);
    }

    @ExceptionHandler(DuplicateVariantTypeException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateVariantType(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }
    @ExceptionHandler(VariantTypeNotFoundException.class)
    public ResponseEntity<Void> handleVariantTypeNotFound() {
        return ResponseEntity.notFound().build();
    }
}