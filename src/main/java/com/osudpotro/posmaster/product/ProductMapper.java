package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.brand.Brand;
import com.osudpotro.posmaster.brand.BrandDto;
import com.osudpotro.posmaster.category.Category;
import com.osudpotro.posmaster.category.CategoryDto;
import com.osudpotro.posmaster.generic.GenericDto;
import com.osudpotro.posmaster.genericunit.GenericUnitDto;
import com.osudpotro.posmaster.manufacturer.Manufacturer;
import com.osudpotro.posmaster.manufacturer.ManufacturerDto;
import com.osudpotro.posmaster.producttype.ProductType;
import com.osudpotro.posmaster.producttype.ProductTypeDto;
import com.osudpotro.posmaster.variantunit.VariantUnitDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMapper {
    //Mapping Here
    //Entity → DTO
    public ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setProductName(product.getProductName());
        productDto.setProductCode(product.getProductCode());
        productDto.setProductBarCode(product.getProductBarCode());
        productDto.setProductSku(product.getProductSku());
        productDto.setDescription(product.getDescription());
        // Manufacturer
        Manufacturer manufacturer = product.getManufacturer();
        if (manufacturer != null) {
            productDto.setManufacturerId(manufacturer.getId());
            ManufacturerDto manufacturerDto = new ManufacturerDto();
            manufacturerDto.setId(manufacturer.getId());
            manufacturerDto.setName(manufacturer.getName());
            productDto.setManufacturer(manufacturerDto);
        }
        //Category
        Category category =product.getCategory();
        if(category!=null){
            productDto.setCategoryId(category.getId());
            CategoryDto categoryDto=new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            productDto.setCategory(categoryDto);
        }
        // Brand
        Brand brand = product.getBrand();
        if (brand != null) {
            productDto.setBrandId(brand.getId());
            BrandDto brandDto = new BrandDto();
            brandDto.setId(brand.getId());
            brandDto.setName(brand.getName());
            productDto.setBrand(brandDto);
        }
        //Product Type
        ProductType productType = product.getProductType();
        if (productType != null) {
            productDto.setProductTypeId(productType.getId());
            ProductTypeDto productTypeDto = new ProductTypeDto();
            productTypeDto.setId(productType.getId());
            productTypeDto.setName(productType.getName());
            productDto.setProductType(productTypeDto);
        }
        // PRODUCT GENERICS
        List<ProductGenericDto> productGenerics = new ArrayList<>();
        for (ProductGeneric productGeneric : product.getProductGenerics()) {
            ProductGenericDto productGenericDto = new ProductGenericDto();
            GenericDto genericDto = new GenericDto();
            genericDto.setId(productGeneric.getGeneric().getId());
            genericDto.setName(productGeneric.getGeneric().getName());
            productGenericDto.setGeneric(genericDto);
            productGenericDto.setDose(productGeneric.getDose());
            GenericUnitDto genericUnitDto = new GenericUnitDto();
            genericUnitDto.setId(productGeneric.getGenericUnit().getId());
            genericUnitDto.setName(productGeneric.getGenericUnit().getName());
            productGenericDto.setGenericUnit(genericUnitDto);
            productGenerics.add(productGenericDto);
        }
        productDto.setProductGenerics(productGenerics);
        // Product Categories
        List<ProductCategoryDto> categories = new ArrayList<>();
        for (ProductCategory productCategory : product.getProductCategories()) {
            ProductCategoryDto productCategoryDto = new ProductCategoryDto();
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(productCategory.getCategory().getId());
            categoryDto.setName(productCategory.getCategory().getName());
            productCategoryDto.setCategory(categoryDto);
//            productCategoryDto.
            categories.add(productCategoryDto);
        }
        productDto.setProductCategories(categories);
        // Product Details
        List<ProductDetailDto> details = new ArrayList<>();
        for (ProductDetail detail : product.getDetails()) {
            ProductDetailDto detailDto = new ProductDetailDto();
            detailDto.setId(detail.getId());
            detailDto.setProductChildCode(detail.getProductDetailCode());
            detailDto.setProductChildBarCode(detail.getProductDetailBarCode());
            detailDto.setProductChildSku(detail.getProductDetailSku());
            detailDto.setRegularPrice(detail.getRegularPrice());
            VariantUnitDto size = new VariantUnitDto();
            size.setId(detail.getSize().getId());
            size.setName(detail.getSize().getName());
            detailDto.setSize(size);
            detailDto.setBulkSize(detail.getBulkSize());
            detailDto.setAtomQty(detail.getAtomQty());
            if (detail.getParentSize() != null) {
                VariantUnitDto parentSize = new VariantUnitDto();
                parentSize.setId(detail.getParentSize().getId());
                parentSize.setName(detail.getParentSize().getName());
                detailDto.setParentSize(parentSize);
            }
            VariantUnitDto color = new VariantUnitDto();
            color.setId(detail.getColor().getId());
            color.setName(detail.getColor().getName());
            detailDto.setColor(color);
            details.add(detailDto);
        }
        productDto.setDetails(details);
        return productDto;
    }
}
