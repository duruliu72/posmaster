package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.variantunit.VariantUnitNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
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
//        try {
//            String data = "789012-20251222-000000001"; // Barcode data
//            String localClientUrl = "http://172.23.160.1:8081/print-barcode"; // local PC IP
//            RestTemplate restTemplate = new RestTemplate();
//            restTemplate.postForObject(localClientUrl, data, String.class);
//        } catch (Exception e) {
//            System.out.println("Connection Failed");
//        }
        return productDetailService.getProductDetail(id);
    }

    @PostMapping("/search-key")
    public PagedResponse<ProductDetailDto> filterProductDetails(
            @RequestBody ProductDetailFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductDetailDto> result = productDetailService.searchByAnyFields(filter, pageable);
        return new PagedResponse<>(result);
    }

    @GetMapping("/next-code/{productId}")
    public String getProductDetailCode(@PathVariable Long productId) {
        return productDetailService.getAllTypeProductDetailNextCode(productId);
    }

    @PostMapping("/create/{productId}")
    public ResponseEntity<ProductDetailDto> createProductDetail(@PathVariable(name = "productId") Long productId, @Valid @RequestBody ProductDetailCreateRequest request, UriComponentsBuilder uriBuilder) {
        var productDetailDto = productDetailService.createProductDetail(productId, request);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDetailDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDetailDto);
    }

    @PostMapping("/update/{productId}/{productDetailId}")
    public ProductDetailDto updateProductDetail(@PathVariable(name = "productId") Long productId, @PathVariable(name = "productDetailId") Long productDetailId, @Valid @RequestBody ProductDetailUpdateRequest request) {
        return productDetailService.updateProductDetail(productId, productDetailId, request);
    }

    @PostMapping("/parents")
    public List<ProductDetailDto> getProductDetailsByParentId(@RequestBody ProductDetailsRequest request) {
        return productDetailService.getProductDetailsByParentId(request);
    }

    @PostMapping("/by_parents")
    public List<ProductDetailDto> getProductDetailsForAddVariant(@RequestBody ProductDetailsRequest request) {
        return productDetailService.getProductDetailsForAddVariant(request);
    }

    @PostMapping(value = "/generate_barcode", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateProductDetailBarCode(@RequestBody ProductDetailsRequest request) throws Exception {
        String code = "(01)09501234567890(10)202512(17)251231";
        return productDetailService.generateProductDetailsBarCodeImage(code, null, null);
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
