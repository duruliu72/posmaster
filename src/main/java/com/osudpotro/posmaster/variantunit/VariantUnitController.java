package com.osudpotro.posmaster.variantunit;

import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.producttype.ProductTypeBulkUpdateRequest;
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
@RequestMapping("/variant-units")
public class VariantUnitController {
    private VariantUnitService variantUnitService;
    @GetMapping
    public List<VariantUnitDto> getAllVariantUnits(){
        return variantUnitService.gerAllVariantUnits();
    }
    @PostMapping("/filter")
    public PagedResponse<VariantUnitDto> filterVariantUnits(
            @RequestBody VariantUnitFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<VariantUnitDto> result = variantUnitService.getVariantUnits(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return variantUnitService.importVariantUnit(file);
    }
    @GetMapping("/{id}")
    public VariantUnitDto getVariantUnit(@PathVariable Long id) {
        return variantUnitService.getVariantUnit(id);
    }
    @PostMapping
    public ResponseEntity<VariantUnitDto> createVariantUnit(@Valid @RequestBody VariantUnitCreateRequest request, UriComponentsBuilder uriBuilder){
        var articleTypeDto = variantUnitService.createVariantUnit(request);
        var uri=uriBuilder.path("/article-units/{id}").buildAndExpand(articleTypeDto.getId()).toUri();
        return  ResponseEntity.created(uri).body(articleTypeDto);
    }
    @PutMapping("/{id}")
    public VariantUnitDto updateVariantUnit(
            @PathVariable(name = "id") Long id,
            @RequestBody VariantUnitUpdateRequest request) {
        return variantUnitService.updateVariantUnit(id, request);
    }
    @DeleteMapping("/{id}")
    public VariantUnitDto deleteVariantUnit(
            @PathVariable(name = "id") Long id) {
        return variantUnitService.deleteVariantUnit(id);
    }
    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkVariantUnit(@RequestBody ProductTypeBulkUpdateRequest request) {
        var count = variantUnitService.deleteBulkVariantUnit(request.getProductTypeIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }
    @GetMapping("/{id}/activate")
    public VariantUnitDto activateVariantUnit(
            @PathVariable(name = "id") Long id) {
        return variantUnitService.activateVariantUnit(id);
    }

    @GetMapping("/{id}/deactivate")
    public VariantUnitDto deactivateVariantUnit(
            @PathVariable(name = "id") Long id) {
        return variantUnitService.deactivateVariantUnit(id);
    }
    @ExceptionHandler(DuplicateVariantUnitException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateVariantUnit(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }
    @ExceptionHandler(VariantUnitNotFoundException.class)
    public ResponseEntity<Void> handleVariantUnitNotFound() {
        return ResponseEntity.notFound().build();
    }
}