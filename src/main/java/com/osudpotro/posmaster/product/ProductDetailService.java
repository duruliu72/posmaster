package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.auth.AuthService;
import com.osudpotro.posmaster.variantunit.VariantUnit;
import com.osudpotro.posmaster.variantunit.VariantUnitNotFoundException;
import com.osudpotro.posmaster.variantunit.VariantUnitRepository;
import com.osudpotro.posmaster.variantunit.VariantUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

//@AllArgsConstructor
@Service
public class ProductDetailService {
    @Autowired
    private  ProductDetailRepository productDetailRepository;
    @Autowired
    private  ProductRepository productRepository;
    @Autowired
    private  VariantUnitRepository variantUnitRepository;
    @Autowired
    private  ProductDetailMapper productDetailMapper;
    @Autowired
    private  AuthService authService;
    @Autowired
    private  VariantUnitService variantUnitService;
    @Value("${gs1-prefix}")
    private String gs1Prefix;
    public ProductDetailDto getProductDetail(Long productDetailId) {
        var productDetail = productDetailRepository.findById(productDetailId).orElseThrow(ProductDetailNotFoundException::new);
        return productDetailMapper.toDto(productDetail);
    }

    public ProductDetailDto createProductDetail(Long productId, ProductDetailCreateRequest request) {
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        var user = authService.getCurrentUser();
        ProductDetail productDetail = new ProductDetail();
        productDetail.setProductDetailCode(request.getProductDetailCode());
        if (request.getProductDetailBarCode() != null) {
            productDetail.setProductDetailBarCode(request.getProductDetailBarCode());
        }
        if (request.getProductDetailSku() != null) {
            productDetail.setProductDetailSku(request.getProductDetailSku());
        }
        if (request.getRegularPrice() != 0) {
            productDetail.setRegularPrice(request.getRegularPrice());
        }
        if (request.getOldPrice() != 0) {
            productDetail.setOldPrice(request.getOldPrice());
        }
        VariantUnit size = null;
        VariantUnit color = null;
        if (request.getSizeId() != null && request.getSizeId() != 0) {
            size = variantUnitRepository.findById(request.getSizeId()).orElseThrow(VariantUnitNotFoundException::new);
        }
        if (request.getColorId() != null && request.getColorId() != 0) {
            color = variantUnitRepository.findById(request.getColorId()).orElseThrow(VariantUnitNotFoundException::new);
        }

//        Check Product Details By Product,Size,Color
        var hasProductDetail = productDetailRepository.findByProductAndSizeAndColor(product, size, color).orElse(null);
        if (hasProductDetail != null) {
            throw new DuplicateProductDetailException();
        } else {
            productDetail.setProduct(product);
            if (size != null) {
                productDetail.setSize(size);
            }
            if (color != null) {
                productDetail.setColor(color);
            }
        }
//        Check Product Detail by Product and Color
        if (productDetailRepository.existsByProductAndColor(product, color)) {
            if (request.getParentProductDetailId() != null && request.getParentProductDetailId() != 0) {
                ProductDetail parentProductDetail = productDetailRepository.findById(request.getParentProductDetailId()).orElseThrow(ProductDetailNotFoundException::new);
                if (parentProductDetail != null) {
                    productDetail.setParentProductDetail(parentProductDetail);
                    if (request.getBulkSize() != 0) {
                        productDetail.setBulkSize(request.getBulkSize());
                        productDetail.setAtomQty(request.getBulkSize() * parentProductDetail.getAtomQty());
                    }
                }
            } else {
                productDetail.setBulkSize(1);
                productDetail.setAtomQty(1);
            }
        } else {
            productDetail.setBulkSize(1);
            productDetail.setAtomQty(1);
        }
        productDetail.setCreatedBy(user);
        productDetailRepository.save(productDetail);
        return productDetailMapper.toDto(productDetail);
    }

    public ProductDetailDto updateProductDetail(Long productId, Long productDetailId, ProductDetailUpdateRequest request) {
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        var user = authService.getCurrentUser();
        var productDetail = productDetailRepository.findById(productDetailId).orElseThrow(ProductDetailNotFoundException::new);
        productDetail.setProductDetailCode(request.getProductDetailCode());
        if (request.getProductDetailBarCode() != null) {
            productDetail.setProductDetailBarCode(request.getProductDetailBarCode());
        }
        if (request.getProductDetailSku() != null) {
            productDetail.setProductDetailSku(request.getProductDetailSku());
        }
        if (request.getRegularPrice() != 0) {
            productDetail.setRegularPrice(request.getRegularPrice());
        }
        if (request.getOldPrice() != 0) {
            productDetail.setOldPrice(request.getOldPrice());
        }
        VariantUnit size = null;
        VariantUnit color = null;
        if (request.getSizeId() != null && request.getSizeId() != 0) {
            size = variantUnitRepository.findById(request.getSizeId()).orElseThrow(VariantUnitNotFoundException::new);
        }
        if (request.getColorId() != null && request.getColorId() != 0) {
            color = variantUnitRepository.findById(request.getColorId()).orElseThrow(VariantUnitNotFoundException::new);
        }
        //        Check Product Details By Product,Size,Color
        var findProductDetail = productDetailRepository.findByProductAndSizeAndColor(product, size, color).orElse(null);
        if (findProductDetail != null && !Objects.equals(findProductDetail.getId(), productDetailId)) {
            throw new DuplicateProductDetailException();
        } else {
//            productDetail.setProduct(product);
            if (size != null) {
                productDetail.setSize(size);
            }
            if (color != null) {
                productDetail.setColor(color);
            }
        }
        //        Check Product Detail by Product and Color
        if (productDetailRepository.existsByProductAndColor(product, color)) {
            if (request.getParentProductDetailId() != null && request.getParentProductDetailId() != 0) {
                ProductDetail parentProductDetail = productDetailRepository.findById(request.getParentProductDetailId()).orElseThrow(ProductDetailNotFoundException::new);
                if (parentProductDetail != null) {
                    productDetail.setParentProductDetail(parentProductDetail);
                    if (request.getBulkSize() != 0) {
                        productDetail.setBulkSize(request.getBulkSize());
                        productDetail.setAtomQty(request.getBulkSize() * parentProductDetail.getAtomQty());
                    }
                }
            } else {
                productDetail.setBulkSize(1);
                productDetail.setAtomQty(1);
            }
        }else {
            productDetail.setBulkSize(1);
            productDetail.setAtomQty(1);
        }
        productDetail.setUpdatedBy(user);
        productDetailRepository.save(productDetail);
        return productDetailMapper.toDto(productDetail);
    }

    public List<ProductDetailDto> getProductDetailsByParentId(ProductDetailsRequest request) {
        productRepository.findById(request.getProductId()).orElseThrow(ProductNotFoundException::new);
        Long parentProductDetailId = 0L;
        Long firstParentProductDetailId = 0L;
        if (request.getProductDetailId() != null) {
            var productDetail = productDetailRepository.findById(request.getProductDetailId()).orElse(null);
            if (productDetail != null) {
                var productDetailsDto = productDetailMapper.toDto(productDetail);
                if (productDetailsDto != null && productDetailsDto.getProductDetailIds() != null) {
                    firstParentProductDetailId = productDetailsDto.getProductDetailIds().getLast();
                }
                if (productDetail.getParentProductDetail() != null) {
                    parentProductDetailId = productDetail.getParentProductDetail().getId();
                }
            }
        }
        return productDetailRepository.findByParentId(request.getProductId(), parentProductDetailId, firstParentProductDetailId)
                .stream()
                .map(productDetailMapper::toDto)
                .toList();
    }

    public List<ProductDetailDto> getProductDetailsForAddVariant(ProductDetailsRequest request) {
        System.out.println(gs1Prefix);
        productRepository.findById(request.getProductId()).orElseThrow(ProductNotFoundException::new);
        Long parentProductDetailId = 0L;
        if (request.getProductDetailId() != null) {
            var productDetail = productDetailRepository.findById(request.getProductDetailId()).orElse(null);
            if (productDetail != null) {
                if (productDetail.getParentProductDetail() != null) {
                    parentProductDetailId = productDetail.getParentProductDetail().getId();
                }
            }
        }
        return productDetailRepository.findProductDetailsByParent(request.getProductId(), request.getProductDetailId(), parentProductDetailId)
                .stream()
                .map(productDetailMapper::toDto)
                .toList();
    }
    public ProductDetailDto generateProductBarCode(){

        return null;
    }
}
