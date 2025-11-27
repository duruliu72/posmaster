package com.osudpotro.posmaster.brand;

import com.osudpotro.posmaster.category.CategoryBulkUpdateRequest;
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
@RequestMapping("/brands")
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    public List<BrandDto> getAllBrands() {
        return brandService.getAllBrands();
    }

    @PostMapping("/filter")
    public PagedResponse<BrandDto> filterBrands(
            @RequestBody BrandFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BrandDto> result = brandService.getBrands(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return brandService.importBrands(file);
    }

    @GetMapping("/{id}")
    public BrandDto getBrand(@PathVariable Long id) {
        return brandService.getBrand(id);
    }

    @PostMapping
    public ResponseEntity<BrandDto> createBrand(@Valid @RequestBody BrandCreateRequest request, UriComponentsBuilder uriBuilder) {
        var brandDto = brandService.createBrand(request);
        var uri = uriBuilder.path("/brands/{id}").buildAndExpand(brandDto.getId()).toUri();
        return ResponseEntity.created(uri).body(brandDto);
    }

    @PutMapping("/{id}")
    public BrandDto updateBrand(
            @PathVariable(name = "id") Long id,
            @RequestBody BrandUpdateRequest request) {
        return brandService.updateBrand(id, request);
    }

    @DeleteMapping("/{id}")
    public BrandDto deleteBrand(
            @PathVariable(name = "id") Long id) {
        return brandService.deleteBrand(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkBrand(@RequestBody BrandBulkUpdateRequest request) {
        var count = brandService.deleteBulkBrand(request.getBrandIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    @GetMapping("/{id}/activate")
    public BrandDto activateBrand(
            @PathVariable(name = "id") Long id) {
        return brandService.activeBrand(id);
    }

    @GetMapping("/{id}/deactivate")
    public BrandDto deactivateBrand(
            @PathVariable(name = "id") Long id) {
        return brandService.deactivateBrand(id);
    }


    @ExceptionHandler(DuplicateBrandException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateBrand(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }

    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<Void> handleBrandNotFound() {
        return ResponseEntity.notFound().build();
    }
}