package com.osudpotro.posmaster.producttype;

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
@RequestMapping("/product-types")
public class ProductTypeController {
    private final ProductTypeService productTypeService;
    @GetMapping
    public List<ProductTypeDto> getAllProductTypes(){
        return productTypeService.gerAllProductTypes();
    }
    @PostMapping("/filter")
    public PagedResponse<ProductTypeDto> filterManufacturers(
            @RequestBody ProductTypeFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductTypeDto> result = productTypeService.getProductTypes(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return productTypeService.importProductType(file);
    }
    @GetMapping("/{id}")
    public ProductTypeDto getProductType(@PathVariable Long id) {
        return productTypeService.getProductType(id);
    }
    @PostMapping
    public ResponseEntity<ProductTypeDto> createProductType(@Valid @RequestBody ProductTypeCreateRequest request, UriComponentsBuilder uriBuilder){
        var productTypeDto = productTypeService.createProductType(request);
        var uri=uriBuilder.path("/product-types/{id}").buildAndExpand(productTypeDto.getId()).toUri();
        return  ResponseEntity.created(uri).body(productTypeDto);
    }
    @PutMapping("/{id}")
    public ProductTypeDto updateProductType(
            @PathVariable(name = "id") Long id,
            @RequestBody ProductTypeUpdateRequest request) {
        return productTypeService.updateProductType(id, request);
    }
    @DeleteMapping("/{id}")
    public ProductTypeDto deleteProductType(
            @PathVariable(name = "id") Long id) {
        return productTypeService.deleteProductType(id);
    }
    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkProductType(@RequestBody ProductTypeBulkUpdateRequest request) {
        var count = productTypeService.deleteBulkProductType(request.getProductTypeIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }
    @GetMapping("/{id}/activate")
    public ProductTypeDto activateProductType(
            @PathVariable(name = "id") Long id) {
        return productTypeService.activateProductType(id);
    }

    @GetMapping("/{id}/deactivate")
    public ProductTypeDto deactivateProductType(
            @PathVariable(name = "id") Long id) {
        return productTypeService.deactivateProductType(id);
    }

    @ExceptionHandler(DuplicateProductTypeException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateProductType(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }
    @ExceptionHandler(ProductTypeNotFoundException.class)
    public ResponseEntity<Void> handleProductTypeNotFound() {
        return ResponseEntity.notFound().build();
    }
}