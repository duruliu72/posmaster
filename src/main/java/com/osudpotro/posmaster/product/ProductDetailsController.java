package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.variantunit.VariantUnitNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/product-details")
public class ProductDetailsController {
    private final ProductDetailService productDetailService;

    @GetMapping("/{id}")
    public ProductDetailDto getProductDetail(@PathVariable Long id) {
        return productDetailService.getProductDetail(id);
    }

    @PostMapping("/create/{productId}")
    public ResponseEntity<ProductDetailDto> createProductDetail(@PathVariable(name = "productId") Long productId, @Valid @RequestBody ProductDetailCreateRequest request, UriComponentsBuilder uriBuilder) {
        var productDetailDto = productDetailService.createProductDetail(productId, request);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDetailDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDetailDto);
    }

    @PostMapping("/update/{productId}/{productDetailId}")
    public ProductDetailDto updateProductDetail(@PathVariable(name = "productId") Long productId, @PathVariable(name = "productDetailId") Long productDetailId, @Valid @RequestBody ProductDetailUpdateRequest request) {
        return productDetailService.updateProductDetail(productId,productDetailId, request);
    }

    @PostMapping("/parents")
    public List<ProductDetailDto> getProductDetailsByParentId(@RequestBody ProductDetailsRequest request) {
        return productDetailService.getProductDetailsByParentId(request);
    }

    @PostMapping("/by_parents")
    public List<ProductDetailDto> getProductDetailsForAddVariant(@RequestBody ProductDetailsRequest request) {
        return productDetailService.getProductDetailsForAddVariant(request);
    }
    @PostMapping("/generate_barcode")
    public ProductDetailDto generateProductBarCode(@RequestBody ProductDetailsRequest request) {
        return productDetailService.generateProductBarCode();
    }
    @ExceptionHandler(ProductDetailNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductDetailNotFound(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("message", e.getMessage())
        );
    }
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Void> handleProductNotFound() {
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(VariantUnitNotFoundException.class)
    public ResponseEntity<Void> handleVariantUnitNotFound() {
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(DuplicateProductDetailException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateProductDetail(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("message", "Product Detail is already exist.")
        );
    }
}
