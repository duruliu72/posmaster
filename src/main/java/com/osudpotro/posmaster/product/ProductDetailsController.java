package com.osudpotro.posmaster.product;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/product-details")
public class ProductDetailsController {
    private final ProductDetailService productDetailService;
    @GetMapping("/{id}")
    public ProductDetailDto getProductDetail(@PathVariable Long id) {
        return productDetailService.getProductDetail(id);
    }
    @PostMapping("/parents")
    public List<ProductDetailDto> getProductDetailsByParentId(@RequestBody ParentProductDetailsRequest request) {
        return productDetailService.getProductDetailsByParentId(request);
    }
}
