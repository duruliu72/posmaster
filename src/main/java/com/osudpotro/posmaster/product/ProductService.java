package com.osudpotro.posmaster.product;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.osudpotro.posmaster.user.auth.AuthService;
import com.osudpotro.posmaster.brand.Brand;
import com.osudpotro.posmaster.brand.BrandRepository;
import com.osudpotro.posmaster.brand.BrandService;
import com.osudpotro.posmaster.category.*;
import com.osudpotro.posmaster.generic.GenericService;
import com.osudpotro.posmaster.genericunit.GenericUnitService;
import com.osudpotro.posmaster.manufacturer.ManufacturerService;
import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.multimedia.MultimediaRepository;
import com.osudpotro.posmaster.producttype.ProductTypeService;
import com.osudpotro.posmaster.variantunit.VariantUnit;
import com.osudpotro.posmaster.variantunit.VariantUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductDetailMapper productDetailMapper;
    @Autowired
    private CustomCategoryMapper customCategoryMapper;
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductTypeService productTypeService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AuthService authService;
    @Autowired
    private GenericService genericService;
    @Autowired
    private GenericUnitService genericUnitService;
    @Autowired
    private ProductGenericService productGenericService;
    @Autowired
    private VariantUnitService variantUnitService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    MultimediaRepository multimediaRepository;
    @Value("${gs1-prefix}")
    private String gs1Prefix;

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    public ProductDto createProduct(ProductCreateRequest request) {
        if (productRepository.existsByProductName(request.getProductName())) {
            throw new DuplicateProductException();
        }
        var user = authService.getCurrentUser();
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setIsPrescribeNeeded(request.getIsPrescribeNeeded());
        String productCode = generateProductCode();
        product.setProductCode(productCode);
        String productBarCode = generateProductBarCode();
        product.setProductBarCode(productBarCode);
        String productSku = generateProductSku(request.getProductName(), request.getBrandId(), request.getCategoryId());
        product.setProductSku(productSku);
        product.setDescription(request.getDescription());
        if (request.getSeoPageName() != null && !request.getSeoPageName().isEmpty()) {
            product.setSeoPageName(request.getSeoPageName().trim().toLowerCase().replace(" ", "-"));
        } else {
            product.setSeoPageName(request.getProductName().trim().toLowerCase().replace(" ", "-"));
        }
        product.setMetaTitle(request.getMetaTitle());
        product.setMetaKeywords(request.getMetaKeywords());
        product.setMetaDescription(request.getMetaDescription());

        if (request.getManufacturerId() != null) {
            var manufacturer = manufacturerService.getManufacturerEntity(request.getManufacturerId());
            product.setManufacturer(manufacturer);
        }
        if (request.getBrandId() != null) {
            var brand = brandService.getBrandEntity(request.getBrandId());
            product.setBrand(brand);
        }
        if (request.getCategoryId() != null) {
            var category = categoryService.getCategoryEntity(request.getCategoryId());
            product.setCategory(category);
        }
        if (request.getProductTypeId() != null) {
            var productType = productTypeService.getProductTypeEntity(request.getProductTypeId());
            product.setProductType(productType);
        }
        if (request.getTags() != null) {
            product.setTags(request.getTags());
        }

        //For ProductGeneric
        List<ProductGeneric> productGenerics = new ArrayList<>();
        if (request.getProductGenerics() != null) {
            for (ProductGenericCreateRequest productGenericRq : request.getProductGenerics()) {
                ProductGeneric productGeneric = new ProductGeneric();
                if (productGenericService.existsProductGeneric(product.getId(), productGenericRq.getGenericId(), productGenericRq.getGenericUnitId())) {
                    throw new DuplicateProductGenericException();
                }
                var generic = genericService.getGenericEntity(productGenericRq.getGenericId());
                productGeneric.setGeneric(generic);
                productGeneric.setDose(productGenericRq.getDose());
                var geneticUnit = genericUnitService.getGenericUnitEntity(productGenericRq.getGenericUnitId());
                productGeneric.setGenericUnit(geneticUnit);
                productGeneric.setCreatedBy(user);
                productGeneric.setProduct(product); // link detail to parent
                productGenerics.add(productGeneric);
            }
        }
        product.setProductGenerics(productGenerics);
        //Product Details
        product.setCreatedBy(user);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    public ProductDto updateProduct(Long productId, ProductUpdateRequest request) {
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        var user = authService.getCurrentUser();
        if (!product.getProductName().equals(request.getProductName()) && productRepository.existsByProductName(request.getProductName())) {
            throw new DuplicateProductException();
        }
        product.setProductName(request.getProductName());
        product.setIsPrescribeNeeded(request.getIsPrescribeNeeded());
        if (request.getSeoPageName() != null && !request.getSeoPageName().isEmpty()) {
            product.setSeoPageName(request.getSeoPageName().trim().toLowerCase().replace(" ", "-"));
        } else {
            product.setSeoPageName(request.getProductName().trim().toLowerCase().replace(" ", "-"));
        }
        product.setMetaTitle(request.getMetaTitle());
        product.setMetaKeywords(request.getMetaKeywords());
        product.setMetaDescription(request.getMetaDescription());
        if (request.getManufacturerId() != null) {
            var manufacturer = manufacturerService.getManufacturerEntity(request.getManufacturerId());
            product.setManufacturer(manufacturer);
        }
        if (request.getBrandId() != null) {
            var brand = brandService.getBrandEntity(request.getBrandId());
            product.setBrand(brand);
        }
        String prescribed = null;
        String medicine = null;
        if (request.getCategoryId() != null) {
            var category = categoryService.getCategoryEntity(request.getCategoryId());
            CategoryDto categoryDto = customCategoryMapper.toDto(category);
            List<String> categoryNameList = Arrays.asList(categoryDto.getFullPath().split(">>"));
            prescribed = categoryNameList.stream().filter(s -> s.trim().equalsIgnoreCase("Prescribed")).findFirst().orElse(null);
            medicine = categoryNameList.stream().filter(s -> s.trim().equalsIgnoreCase("Medicine")).findFirst().orElse(null);
            if (prescribed != null && !prescribed.equalsIgnoreCase("Prescribed")) {
                product.setIsPrescribeNeeded(false);
            }
            product.setCategory(category);
        }
        if (request.getProductTypeId() != null) {
            var productType = productTypeService.getProductTypeEntity(request.getProductTypeId());
            product.setProductType(productType);
        }
        if (request.getTags() != null) {
            product.setTags(request.getTags());
        }
        product.setUpdatedBy(user);
        //For ProductGeneric
        //For Total ProductGeneric List
        List<ProductGeneric> productGenerics = product.getProductGenerics();
//        Previous ProductGeneric List
        List<ProductGeneric> previousPGList = new ArrayList<>();
        if (request.getProductGenerics() != null) {
            for (ProductGenericUpdateRequest productGenericRq : request.getProductGenerics()) {
                ProductGeneric productGeneric = productGenerics.stream()
                        .filter(item -> item.getGeneric().getId().equals(productGenericRq.getGenericId()) && item.getGenericUnit().getId().equals(productGenericRq.getGenericUnitId()))
                        .findFirst()
                        .orElse(null);
                if (productGeneric != null && !productGeneric.getGeneric().getId().equals(productGenericRq.getGenericId()) && !productGeneric.getGenericUnit().getId().equals(productGenericRq.getGenericUnitId())) {
                    if (productGenericService.existsProductGeneric(product.getId(), productGenericRq.getGenericId(), productGenericRq.getGenericUnitId())) {
                        throw new DuplicateProductGenericException();
                    }
                }
                if (productGeneric == null) {
                    productGeneric = new ProductGeneric();
                    productGeneric.setIsNew(true);
                } else {
                    previousPGList.add(productGeneric);
                }
                var generic = genericService.getGenericEntity(productGenericRq.getGenericId());
                productGeneric.setGeneric(generic);
                productGeneric.setDose(productGenericRq.getDose());
                var geneticUnit = genericUnitService.getGenericUnitEntity(productGenericRq.getGenericUnitId());
                productGeneric.setGenericUnit(geneticUnit);
                productGeneric.setCreatedBy(user);
                productGeneric.setProduct(product); // link detail to parent
                if (productGeneric.getIsNew()) {
                    productGenerics.add(productGeneric);
                }
            }
        }

        for (ProductGeneric item : productGenerics) {
            ProductGeneric productGeneric = previousPGList.stream()
                    .filter(pGItem -> {
                        if (item.getId() == null) {
                            return true;
                        }
                        return item.getGeneric().getId().equals(pGItem.getGeneric().getId()) && item.getGenericUnit().getId().equals(pGItem.getGenericUnit().getId());
                    })
                    .findFirst()
                    .orElse(null);
            if (productGeneric != null || item.getIsNew()) {
                //                Set Item as active
                if (medicine != null && medicine.equalsIgnoreCase("Medicine")) {
                    item.setStatus(1);
                } else {
                    item.setStatus(2);
                }
            } else {
                // Set Item as inactive
                item.setStatus(2);
            }
        }
        product.setProductGenerics(productGenerics);
//        product Multimedia Image
        if (request.getMultimediaId() != null) {
            Multimedia multimedia = multimediaRepository.findById(request.getMultimediaId()).orElse(null);
            if (multimedia != null) {
                multimedia.setLinked(true);
                product.setMedia(multimedia);
            }
        }
        //Product Details
        List<ProductDetail> details = product.getDetails();
        List<ProductDetail> detailList = new ArrayList<>();
        if (request.getDetails() != null) {
            int lastAtomQty = 1;
            for (ProductDetailUpdateRequest detailRq : request.getDetails()) {
                boolean isNewChild = false;
                ProductDetail lastProductDetail = IntStream.range(0, detailList.size())
                        .mapToObj(i -> detailList.get(detailList.size() - 1 - i))
                        .filter(p -> p.getColor().getId().equals(detailRq.getColorId()))
                        .findFirst()
                        .orElse(null);
                if (lastProductDetail != null) {
//                    lastAtomQty = lastProductDetail.getAtomQty();
//                    if (!lastProductDetail.getSize().getId().equals(detailRq.getParent_size_id())) {
//                        throw new ParentVariantSizeException();
//                    }
                } else {
                    lastAtomQty = 1;
                }
                ProductDetail detail = details.stream()
                        .filter(item -> item.getSize().getId().equals(detailRq.getSizeId()) && item.getColor().getId().equals(detailRq.getColorId()))
                        .findFirst()
                        .orElse(null);
                if (detail == null) {
                    detail = new ProductDetail();
                    isNewChild = true;
                    detail.setCreatedBy(user);
                }
                detail.setProductDetailCode(detailRq.getProductDetailCode());
                detail.setProductDetailBarCode(detailRq.getProductDetailBarCode());
                detail.setProductDetailSku(detailRq.getProductDetailSku());
                detail.setSellPrice(detailRq.getSellPrice());
                detail.setMrpPrice(detailRq.getMrpPrice());
                VariantUnit size = variantUnitService.getVariantUnitEntity(detailRq.getSizeId());
                detail.setSize(size);
                if (lastProductDetail != null) {
                    detail.setBulkSize(detailRq.getBulkSize());
                    lastAtomQty = detailRq.getBulkSize() * lastAtomQty;
                } else {
                    detail.setBulkSize(1);
                }
                detail.setAtomQty(lastAtomQty);
//                if (lastProductDetail != null && detailRq.getParent_size_id() != null) {
//                    VariantUnit parentSize = variantUnitService.getVariantUnitEntity(detailRq.getParent_size_id());
//                    detail.setParentSize(parentSize);
//                }
                VariantUnit color = variantUnitService.getVariantUnitEntity(detailRq.getColorId());
                detail.setColor(color);
                detail.setUpdatedBy(user);
                detail.setProduct(product); // link detail to parent
                if (isNewChild) {
                    details.add(detail);
                }
                detailList.add(detail);
            }
        }
        product.setDetails(details);
        product.setCreatedBy(user);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    public ProductDto getProduct(Long productId) {
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        return productMapper.toDto(product);
    }

    public ProductDto findByIdWithProductGenericStatus(Long productId) {
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        var list = product.getProductGenerics().stream().filter(item -> item.getStatus() == 1).toList();
        product.setProductGenerics(list);
        return productMapper.toDto(product);
    }

    public Product getProductEntity(Long productId) {
        return productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    }

    public Page<ProductDto> getFilterProducts(ProductFilter filter, Pageable pageable) {
        if (filter.getCategoryId() != null && filter.getSearchIncludeSubCategories()) {
            List<Long> childrenIds = new ArrayList<>();
            CategoryDto categoryDto = categoryService.getCategoryOrNull(filter.getCategoryId());
            if (categoryDto.getId() != null) {
                childrenIds.add(categoryDto.getId());
                List<Long> moreChildrenIds = categoryService.getChildrenIds(categoryDto.getId());
                if (moreChildrenIds != null && !moreChildrenIds.isEmpty()) {
                    childrenIds.addAll(moreChildrenIds);
                }
                filter.setChildCategoryIds(childrenIds);
            }
        }
        return productRepository.findAll(ProductSpecification.filter(filter), pageable).map(productMapper::toDto);
//        Page<Product> result = productRepository.findAll(ProductSpecification.filter(filter), pageable);
//        List<Product> products = result.getContent();
//        List<ProductDetail> filteredDetails = productDetailRepository.findAll(ProductDetailSpecification.filter(filter));
//        List<ProductDto> newResult = products.stream()
//                .map(p -> {
//                    List<ProductDetailDto> detailDTOs = filteredDetails.stream()
//                            .filter(d -> d.getProduct().getId().equals(p.getId())).map(productDetailMapper::toDto).toList();
//                    ProductDto newProductDto = productMapper.toDto(p);
//                    newProductDto.setDetails(detailDTOs);
//                    return newProductDto;
//                }).toList();
//        return new PageImpl<>(
//                        newResult,
//                        result.getPageable(),
//                        result.getTotalElements()
//                );
//        return newPage;
    }

    private String generateProductCode() {
        // Date part
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // Prefix from category
        Product product = productRepository.findTopByOrderByCreatedAtDesc();
        String prefix = "PRO";
        // Extract sequence number from last product code
        long nextSeq = 1;
        if (product != null && product.getProductCode() != null) {
            String lastProductCode = product.getProductCode();
            String[] parts = lastProductCode.split("-");
            if (parts.length > 2) {
                nextSeq = Long.parseLong(parts[2]) + 1;
            }
        }
        //Format Product code
        return String.format("%s-%s-%09d", prefix, datePart, nextSeq);
    }

    public String generateProductSku(String productName, Long brandId, Long categoryId) {
        Product productDetail = productRepository.findTopByOrderByCreatedAtDesc();
        Brand brand = null;
        if (brandId != null) {
            brand = brandRepository.findById(brandId).orElse(null);
        }
        Category category = categoryRepository.findById(categoryId).orElse(null);
        String brandPrefix = "";
        if (brand != null) {
            brandPrefix = brand.getName().length() >= 3 ? brand.getName().substring(0, 3) : brand.getName();
        }
        String categoryPrefix = "";
        if (category != null) {
            categoryPrefix = category.getName().length() >= 3 ? category.getName().substring(0, 3) : category.getName();
        }
        String productPrefix = "";
        if (productName != null) {
            productPrefix = productName.length() >= 3 ? productName.substring(0, 3) : productName;
        }
        long nextSeq = 1;
        if (productDetail != null && productDetail.getProductSku() != null) {
            String lastProductDetailsSku = productDetail.getProductSku();
            String[] parts = lastProductDetailsSku.split("-");
            if (brandPrefix.isEmpty()) {
                if (parts.length > 2) {
                    nextSeq = Long.parseLong(parts[2]) + 1;
                }
            } else {
                if (parts.length > 3) {
                    nextSeq = Long.parseLong(parts[3]) + 1;
                }
            }
        }
        //Format String
        if (brandPrefix.isEmpty()) {
            return String.format("%s-%s-%09d", categoryPrefix, productPrefix, nextSeq);
        }
        return String.format("%s-%s-%s-%09d", brandPrefix, categoryPrefix, productPrefix, nextSeq);
    }

    public String generateProductBarCode() {
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        Product productDetail = productRepository.findTopByOrderByCreatedAtDesc();
        long nextSeq = 1;
        if (productDetail != null && productDetail.getProductBarCode() != null) {
            String lastProductBarCode = productDetail.getProductBarCode();
            String lastPart = lastProductBarCode.length() > 8 ? lastProductBarCode.substring(lastProductBarCode.length() - 9) : lastProductBarCode;
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

    public byte[] generateProductBarCodeImage(String code, Integer customHeight, Integer customWidth) throws Exception {
        int width = customWidth != null ? customWidth : 400;
        int height = customHeight != null ? customHeight : 150;
        BitMatrix matrix = new MultiFormatWriter()
                .encode(code, BarcodeFormat.CODE_128, width, height);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
        return baos.toByteArray();
    }
}
