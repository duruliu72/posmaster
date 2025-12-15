package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.brand.BrandNotFoundException;
import com.osudpotro.posmaster.category.CategoryNotFoundException;
import com.osudpotro.posmaster.category.CategoryService;
import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.generic.GenericNotFoundException;
import com.osudpotro.posmaster.genericunit.GenericUnitNotFoundException;
import com.osudpotro.posmaster.manufacturer.ManufacturerNotFoundException;
import com.osudpotro.posmaster.producttype.ProductTypeNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/filter")
    public PagedResponse<ProductDto> filterProducts(
            @RequestBody ProductFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductDto> result = productService.getFilterProducts(filter, pageable);
        return new PagedResponse<>(result);
    }

    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductCreateRequest request, UriComponentsBuilder uriBuilder) {
        var productDto = productService.createProduct(request);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(
            @PathVariable(name = "id") Long id,
            @RequestBody ProductUpdateRequest request) {
        return productService.updateProduct(id, request);
    }

    @ExceptionHandler(DuplicateProductException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateProduct(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("message", "Name is already exist.")
        );
    }

    @ExceptionHandler(ParentVariantSizeException.class)
    public ResponseEntity<Map<String, String>> handleParentVariantSizeProduct(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("message", "Parent Variant Size Mismatch.")
        );
    }

    @ExceptionHandler(DuplicateProductGenericException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateProductGeneric(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("message", "Product Generic is already exist.")
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Void> handleProductNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ManufacturerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleManufacturerNotFound(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("message", e.getMessage())
        );
    }

    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBrandNotFound(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("message", e.getMessage())
        );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCategoryNotFound(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("message", e.getMessage())
        );
    }

    @ExceptionHandler(ProductTypeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductTypeNotFound(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("message", e.getMessage())
        );
    }

    @ExceptionHandler(GenericNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleGenericNotFound(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("message", e.getMessage())
        );
    }

    @ExceptionHandler(GenericUnitNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleGenericUnitNotFound(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("message", e.getMessage())
        );
    }
}
