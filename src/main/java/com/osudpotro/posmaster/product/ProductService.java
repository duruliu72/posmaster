package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.auth.AuthService;
import com.osudpotro.posmaster.brand.BrandService;
import com.osudpotro.posmaster.category.Category;
import com.osudpotro.posmaster.category.CategoryDto;
import com.osudpotro.posmaster.category.CategoryService;
import com.osudpotro.posmaster.category.CustomCategoryMapper;
import com.osudpotro.posmaster.generic.GenericService;
import com.osudpotro.posmaster.genericunit.GenericUnitService;
import com.osudpotro.posmaster.manufacturer.ManufacturerService;
import com.osudpotro.posmaster.producttype.ProductTypeService;
import com.osudpotro.posmaster.variantunit.VariantUnit;
import com.osudpotro.posmaster.variantunit.VariantUnitService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ManufacturerService manufacturerService;
    private final BrandService brandService;
    private final ProductTypeService productTypeService;
    private final CategoryService categoryService;
    private final AuthService authService;
    private final GenericService genericService;
    private final GenericUnitService genericUnitService;
    private final ProductGenericService productGenericService;
    private final CustomCategoryMapper customCategoryMapper;
    private final VariantUnitService variantUnitService;

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
        var manufacturer = manufacturerService.getManufacturerEntity(request.getManufacturerId());
        var brand = brandService.getBrandEntity(request.getBrandId());
        var productType = productTypeService.getProductTypeEntity(request.getProductTypeId());
        Category category = categoryService.getCategoryEntity(request.getCategoryId());
        var user = authService.getCurrentUser();
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setProductCode(request.getProductCode());
        product.setProductBarCode(request.getProductBarCode());
        product.setProductSku(request.getProductSku());
        product.setDescription(request.getDescription());
        product.setManufacturer(manufacturer);
        product.setBrand(brand);
        product.setCategory(category);
        product.setProductType(productType);

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
        List<ProductDetail> details = new ArrayList<>();
        if (request.getDetails() != null) {
            int lastAtomQty = 1;
            for (ProductDetailCreateRequest detailRq : request.getDetails()) {
                ProductDetail lastProductDetail = IntStream.range(0, details.size())
                        .mapToObj(i -> details.get(details.size() - 1 - i)) // reverse
                        .filter(p -> p.getColor().getId().equals(detailRq.getColorId()))
                        .findFirst()
                        .orElse(null);
                if (lastProductDetail != null) {
                    lastAtomQty = lastProductDetail.getAtomQty();
                    if (detailRq.getParent_size_id() != null) {
                        if (!lastProductDetail.getSize().getId().equals(detailRq.getParent_size_id())) {
                            throw new ParentVariantSizeException();
                        }
                    } else {
                        lastAtomQty = 1;
                    }
                } else {
                    lastAtomQty = 1;
                }
                ProductDetail detail = new ProductDetail();
                detail.setProductDetailCode(detailRq.getProductDetailCode());
                detail.setProductDetailBarCode(detailRq.getProductDetailBarCode());
                detail.setProductDetailSku(detailRq.getProductDetailSku());
                detail.setRegularPrice(detailRq.getRegularPrice());
                detail.setOldPrice(detailRq.getOldPrice());
                VariantUnit size = variantUnitService.getVariantUnitEntity(detailRq.getSizeId());
                detail.setSize(size);
                if (lastProductDetail != null) {
                    detail.setBulkSize(detailRq.getBulkSize());
                    lastAtomQty = detailRq.getBulkSize() * lastAtomQty;
                } else {
                    detail.setBulkSize(1);
                }
                detail.setAtomQty(lastAtomQty);
                if (lastProductDetail != null && detailRq.getParent_size_id() != null) {
                    VariantUnit parentSize = variantUnitService.getVariantUnitEntity(detailRq.getParent_size_id());
                    detail.setParentSize(parentSize);
                }
                VariantUnit color = variantUnitService.getVariantUnitEntity(detailRq.getColorId());
                detail.setColor(color);
                detail.setCreatedBy(user);
                detail.setProduct(product); // link detail to parent
                details.add(detail);
            }
        }
        product.setDetails(details);
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
        var manufacturer = manufacturerService.getManufacturerEntity(request.getManufacturerId());
        var brand = brandService.getBrandEntity(request.getBrandId());
        var category = categoryService.getCategoryEntity(request.getCategoryId());
        var productType = productTypeService.getProductTypeEntity(request.getProductTypeId());
        product.setProductName(request.getProductName());
        product.setProductCode(request.getProductCode());
        product.setProductBarCode(request.getProductBarCode());
        product.setProductSku(request.getProductSku());
        product.setDescription(request.getDescription());
        product.setManufacturer(manufacturer);
        product.setBrand(brand);
        product.setCategory(category);
        product.setProductType(productType);
        product.setUpdatedBy(user);
        //For ProductGeneric
        List<ProductGeneric> productGenerics = product.getProductGenerics();
        List<ProductGeneric> filteredList = new ArrayList<>();
        if (request.getProductGenerics() != null) {
            boolean isPgNew=false;
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
                    isPgNew=true;
                } else {
                    filteredList.add(productGeneric);
                }
                var generic = genericService.getGenericEntity(productGenericRq.getGenericId());
                productGeneric.setGeneric(generic);
                productGeneric.setDose(productGenericRq.getDose());
                var geneticUnit = genericUnitService.getGenericUnitEntity(productGenericRq.getGenericUnitId());
                productGeneric.setGenericUnit(geneticUnit);
                productGeneric.setCreatedBy(user);
                productGeneric.setProduct(product); // link detail to parent
                if (isPgNew) {
                    productGenerics.add(productGeneric);
                }
            }
        }
        for (ProductGeneric item : productGenerics) {
            ProductGeneric productGeneric = filteredList.stream()
                    .filter(pGItem -> item.getGeneric().getId().equals(pGItem.getGeneric().getId()) && item.getGenericUnit().getId().equals(pGItem.getGenericUnit().getId()))
                    .findFirst()
                    .orElse(null);
            if (productGeneric ==null){
                item.setStatus(2);
            }else {
                item.setStatus(1);
            }
        }
        product.setProductGenerics(productGenerics);
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
                    lastAtomQty = lastProductDetail.getAtomQty();
                    if (!lastProductDetail.getSize().getId().equals(detailRq.getParent_size_id())) {
                        throw new ParentVariantSizeException();
                    }
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
                detail.setRegularPrice(detailRq.getRegularPrice());
                detail.setOldPrice(detailRq.getOldPrice());
                VariantUnit size = variantUnitService.getVariantUnitEntity(detailRq.getSizeId());
                detail.setSize(size);
                if (lastProductDetail != null) {
                    detail.setBulkSize(detailRq.getBulkSize());
                    lastAtomQty = detailRq.getBulkSize() * lastAtomQty;
                } else {
                    detail.setBulkSize(1);
                }
                detail.setAtomQty(lastAtomQty);
                if (lastProductDetail != null && detailRq.getParent_size_id() != null) {
                    VariantUnit parentSize = variantUnitService.getVariantUnitEntity(detailRq.getParent_size_id());
                    detail.setParentSize(parentSize);
                }
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

    public Product getProductEntity(Long productId) {
        return productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    }
    public Page<ProductDto> getFilterProducts(ProductFilter filter, Pageable pageable) {
        if(filter.getCategoryId()!=null && filter.getSearchIncludeSubCategories()){
            List<Long> childrenIds=new ArrayList<>();
            CategoryDto categoryDto=categoryService.getCategoryOrNull(filter.getCategoryId());
            if(categoryDto.getId()!=null){
                childrenIds.add(categoryDto.getId());
                List<Long> moreChildrenIds=categoryService.getChildrenIds(categoryDto.getId());
                if (moreChildrenIds != null && !moreChildrenIds.isEmpty()) {
                    childrenIds.addAll(moreChildrenIds);
                }
                filter.setChildCategoryIds(childrenIds);
            }
        }
        return productRepository.findAll(ProductSpecification.filter(filter), pageable).map(productMapper::toDto);
    }
}
