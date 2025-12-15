package com.osudpotro.posmaster.product;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ProductDetailService {
    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;
    private final ProductDetailMapper productDetailMapper;

    public ProductDetailDto getProductDetail(Long productDetailId) {
        var productDetail = productDetailRepository.findById(productDetailId).orElseThrow(ProductDetailNotFoundException::new);
        return productDetailMapper.toDto(productDetail);
    }

    public List<ProductDetailDto> getProductDetailsByParentId(ParentProductDetailsRequest request) {
        productRepository.findById(request.getProductId()).orElseThrow(ProductNotFoundException::new);
        List<Long> productDetailIds = new ArrayList<>();
        Long sizeId = 0L;
        if (request.getProductDetailId() != null) {
            var productDetail = productDetailRepository.findById(request.getProductDetailId()).orElse(null);
            if (productDetail != null) {
                sizeId = productDetail.getSize().getId();
//                var productDetailsDto = productDetailMapper.toDto(productDetail);
//                if (productDetailsDto != null && productDetailsDto.getProductDetailIds() != null) {
//                    productDetailIds.addAll(productDetailsDto.getProductDetailIds());
//                    productDetailIds.remove(productDetailsDto.getId());
//                }
                if (productDetail.getParentProductDetail() != null) {
                    productDetailIds.add(productDetail.getParentProductDetail().getId());
                }
            }
        }
        return productDetailRepository.findByParentId(request.getProductId(), productDetailIds, sizeId)
                .stream()
                .map(productDetailMapper::toDto)
                .toList();
    }
}
