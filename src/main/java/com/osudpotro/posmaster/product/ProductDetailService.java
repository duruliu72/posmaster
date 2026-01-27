package com.osudpotro.posmaster.product;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.osudpotro.posmaster.auth.AuthService;
import com.osudpotro.posmaster.brand.Brand;
import com.osudpotro.posmaster.brand.BrandRepository;
import com.osudpotro.posmaster.category.Category;
import com.osudpotro.posmaster.category.CategoryRepository;
import com.osudpotro.posmaster.product.pricinglog.ProductPricingLog;
import com.osudpotro.posmaster.product.pricinglog.ProductPricingLogRepository;
import com.osudpotro.posmaster.variantunit.VariantUnit;
import com.osudpotro.posmaster.variantunit.VariantUnitNotFoundException;
import com.osudpotro.posmaster.variantunit.VariantUnitRepository;
import com.osudpotro.posmaster.variantunit.VariantUnitService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

//@AllArgsConstructor
@Service
public class ProductDetailService {
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductPricingLogRepository productPricingLogRepository;
    @Autowired
    private VariantUnitRepository variantUnitRepository;
    @Autowired
    private ProductDetailMapper productDetailMapper;
    @Autowired
    private AuthService authService;
    @Autowired
    private VariantUnitService variantUnitService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Value("${gs1-prefix}")
    private String gs1Prefix;

    public ProductDetailDto getProductDetail(Long productDetailId) {
        var productDetail = productDetailRepository.findById(productDetailId).orElseThrow(ProductDetailNotFoundException::new);
        return productDetailMapper.toDto(productDetail);
    }
    public Page<ProductDetailDto> searchByAnyFields(ProductDetailFilter filter, Pageable pageable) {
        return productDetailRepository.searchByAnyFields(filter.getSearchKey(), pageable).map(productDetailMapper::toDto);
    }
    public String getAllTypeProductDetailNextCode(Long productId) {
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        String productDetailCode = generateProductDetailCode(product.getCategory().getId());
        String productDetailBarCode = generateProductDetailsBarCode();
        return "";
    }

    public ProductDetailDto createProductDetail(Long productId, ProductDetailCreateRequest request) {
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        var user = authService.getCurrentUser();

        ProductDetail productDetail = new ProductDetail();
        if (product.getCategory() != null) {
            String productDetailCode = generateProductDetailCode(product.getCategory().getId());
            productDetail.setProductDetailCode(productDetailCode);
        }
        String productDetailBarCode = generateProductDetailsBarCode();
        productDetail.setProductDetailBarCode(productDetailBarCode);
        if (request.getRegularPrice() != null) {
            productDetail.setRegularPrice(request.getRegularPrice());
        }
        if (request.getOldPrice() != null) {
            productDetail.setOldPrice(request.getOldPrice());
        }
        if (request.getPurchasePrice() != null) {
            productDetail.setPurchasePrice(request.getPurchasePrice());
        }
        VariantUnit size = null;
        VariantUnit color = null;
        if (request.getSizeId() != null && request.getSizeId() != 0) {
            size = variantUnitRepository.findById(request.getSizeId()).orElseThrow(VariantUnitNotFoundException::new);
        }
        if (request.getColorId() != null && request.getColorId() != 0) {
            color = variantUnitRepository.findById(request.getColorId()).orElseThrow(VariantUnitNotFoundException::new);
        }
        Long brandId = 0L;
        if (product.getBrand() != null) {
            brandId = product.getBrand().getId();
        }
        if (size != null && color == null) {
            String productDetailSku = generateProductDetailSku(product.getId(), brandId, product.getCategory().getId(), size.getId());
            productDetail.setProductDetailSku(productDetailSku);
        }
        if (size != null && color != null) {
            String productDetailSku = generateProductDetailSku(product.getId(), brandId, product.getCategory().getId(), size.getId(), color.getId());
            productDetail.setProductDetailSku(productDetailSku);
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
        productDetail.setBulkSize(1);
        productDetail.setAtomQty(1);
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
        }
        productDetail.setCreatedBy(user);
        productDetailRepository.save(productDetail);
        return productDetailMapper.toDto(productDetail);
    }
    @Transactional
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
//        Log the ProductPricingLog if regularPrice,oldPrice and purchasePrice changed
        if(!Objects.equals(productDetail.getRegularPrice(), request.getRegularPrice())||!Objects.equals(productDetail.getOldPrice(),request.getOldPrice())||!Objects.equals(productDetail.getPurchasePrice(),request.getPurchasePrice())){
            ProductPricingLog productPricingLog=new ProductPricingLog();
            productPricingLog.setRegularPrice(productDetail.getRegularPrice());
            productPricingLog.setOldPrice(productDetail.getOldPrice());
            productPricingLog.setPurchasePrice(productDetail.getPurchasePrice());
            productPricingLog.setProduct(product);
            productPricingLog.setProductDetail(productDetail);
            productPricingLog.setCreatedBy(user);
            productPricingLogRepository.save(productPricingLog);
        }
        if (request.getRegularPrice() != null) {
            productDetail.setRegularPrice(request.getRegularPrice());
        }
        if (request.getOldPrice() != null) {
            productDetail.setOldPrice(request.getOldPrice());
        }
        if (request.getPurchasePrice() != null) {
            productDetail.setPurchasePrice(request.getPurchasePrice());
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
        } else {
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
                    firstParentProductDetailId = productDetailsDto.getProductDetailIds().get(productDetailsDto.getProductDetailIds().size() - 1);
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

    public String generateProductDetailsBarCode() {
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        ProductDetail productDetail = productDetailRepository.findTopByOrderByCreatedAtDesc();
        long nextSeq = 1;
        if (productDetail != null) {
            String lastProductDetailsBarCode = productDetail.getProductDetailBarCode();
            String lastPart = lastProductDetailsBarCode.length() > 8 ? lastProductDetailsBarCode.substring(lastProductDetailsBarCode.length() - 9) : lastProductDetailsBarCode;
            if (!lastPart.isEmpty()) {
                try {
                    nextSeq = Long.parseLong(lastPart) + 1;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        //Format String
        return String.format("%s-%s-%09d", gs1Prefix, datePart, nextSeq);
    }

    public byte[] generateProductDetailsBarCodeImage(String code, Integer customHeight, Integer customWidth) throws Exception {
        int width = customWidth != null ? customWidth : 400;
        int height = customHeight != null ? customHeight : 150;
        BitMatrix matrix = new MultiFormatWriter()
                .encode(code, BarcodeFormat.CODE_128, width, height);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
        return baos.toByteArray();
    }

    private String generateProductDetailCode(Long categoryId) {
        // Date part
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // Prefix from category
        Category category = categoryRepository.findById(categoryId).orElse(null);
        ProductDetail productDetail = productDetailRepository.findTopByOrderByCreatedAtDesc();
        String prefix = "PRO";
        if (category != null) {
            prefix = category.getName().length() >= 3 ? category.getName().substring(0, 3).toUpperCase() : category.getName().toUpperCase();
        }
        // Extract sequence number from last product code
        long nextSeq = 1;
        if (productDetail != null) {
            String lastProductCode = productDetail.getProductDetailCode();
            String[] parts = lastProductCode.split("-");
            if (parts.length > 2) {
                nextSeq = Long.parseLong(parts[2]) + 1;
            }
        }
        //Format Product code
        return String.format("%s-%s-%09d", prefix, datePart, nextSeq);
    }

    public String generateProductDetailSku(Long productId, Long brandId, Long categoryId, Long sizeId) {
        ProductDetail productDetail = productDetailRepository.findTopByOrderByCreatedAtDesc();
        Brand brand = brandRepository.findById(brandId).orElse(null);
        Category category = categoryRepository.findById(categoryId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);
        VariantUnit size = variantUnitRepository.findById(sizeId).orElse(null);
        String brandPrefix = "";
        if (brand != null) {
            brandPrefix = brand.getName().length() >= 3 ? brand.getName().substring(0, 3) : brand.getName();
        }
        String categoryPrefix = "";
        if (category != null) {
            categoryPrefix = category.getName().length() >= 3 ? category.getName().substring(0, 3) : category.getName();
        }
        String productPrefix = "";
        if (product != null) {
            productPrefix = product.getProductName().length() >= 3 ? product.getProductName().substring(0, 3) : product.getProductName();
        }
        String sizePrefix = "";
        if (size != null) {
            sizePrefix = size.getName().length() >= 3 ? size.getName().substring(0, 3) : size.getName();
        }
        long nextSeq = 1;
        if (productDetail != null) {
            String lastProductDetailsSku = productDetail.getProductDetailSku();
            String[] parts = lastProductDetailsSku.split("-");
            if (brandPrefix.isEmpty()) {
                if (parts.length > 3) {
                    nextSeq = Long.parseLong(parts[3]) + 1;
                }
            } else {
                if (parts.length > 4) {
                    nextSeq = Long.parseLong(parts[4]) + 1;
                }
            }

        }
        //Format String
        if (brandPrefix.isEmpty()) {
            return String.format("%s-%s-%s-%09d", categoryPrefix, productPrefix, sizePrefix, nextSeq);
        }
        return String.format("%s-%s-%s-%s-%09d", brandPrefix, categoryPrefix, productPrefix, sizePrefix, nextSeq);
    }

    public String generateProductDetailSku(Long productId, Long brandId, Long categoryId, Long sizeId, Long colorId) {
        ProductDetail productDetail = productDetailRepository.findTopByOrderByCreatedAtDesc();
        Brand brand = brandRepository.findById(brandId).orElse(null);
        Category category = categoryRepository.findById(categoryId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);
        VariantUnit size = variantUnitRepository.findById(sizeId).orElse(null);
        VariantUnit color = variantUnitRepository.findById(colorId).orElse(null);
        String brandPrefix = "";
        if (brand != null) {
            brandPrefix = brand.getName().length() >= 3 ? brand.getName().substring(0, 3) : brand.getName();
        }
        String categoryPrefix = "";
        if (category != null) {
            categoryPrefix = category.getName().length() >= 3 ? category.getName().substring(0, 3) : category.getName();
        }
        String productPrefix = "";
        if (product != null) {
            productPrefix = product.getProductName().length() >= 3 ? product.getProductName().substring(0, 3) : product.getProductName();
        }
        String sizePrefix = "";
        if (size != null) {
            sizePrefix = size.getName().length() >= 3 ? size.getName().substring(0, 3) : size.getName();
        }
        String colorPrefix = "";
        if (color != null) {
            colorPrefix = color.getName().length() >= 3 ? color.getName().substring(0, 3) : color.getName();
        }
        long nextSeq = 1;
        if (productDetail != null) {
            String lastProductDetailsSku = productDetail.getProductDetailSku();
            String[] parts = lastProductDetailsSku.split("-");
            if (brandPrefix.isEmpty()) {
                if (parts.length > 4) {
                    nextSeq = Long.parseLong(parts[4]) + 1;
                }
            } else {
                if (parts.length > 5) {
                    nextSeq = Long.parseLong(parts[5]) + 1;
                }
            }
        }
        //Format String
        if (brandPrefix.isEmpty()) {
            return String.format("%s-%s-%s-%s-%09d", categoryPrefix, productPrefix, sizePrefix, colorPrefix, nextSeq);
        }
        return String.format("%s-%s-%s-%s-%s-%09d", brandPrefix, categoryPrefix, productPrefix, sizePrefix, colorPrefix, nextSeq);
    }

}
