package com.osudpotro.posmaster.product;

import com.osudpotro.posmaster.brand.Brand;
import com.osudpotro.posmaster.category.Category;
import com.osudpotro.posmaster.manufacturer.Manufacturer;
import com.osudpotro.posmaster.producttype.ProductType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> filter(ProductFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getProductName() != null && !filter.getProductName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("productName")),
                        "%" + filter.getProductName().toLowerCase() + "%"));
            }
            Join<Product, ProductDetail> details = root.join("details", JoinType.LEFT);
            if (filter.getProductCode() != null && !filter.getProductCode().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("productCode")),
                        "%" + filter.getProductCode().toLowerCase() + "%"));
            }
            if (filter.getProductBarCode() != null && !filter.getProductBarCode().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("productBarCode")),
                        "%" + filter.getProductBarCode().toLowerCase() + "%"));
            }
            if (filter.getProductSku() != null && !filter.getProductSku().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("productSku")),
                        "%" + filter.getProductSku().toLowerCase() + "%"));
            }
            if (filter.getCategoryId() != null) {
                Join<Product, Category> cat = root.join("category", JoinType.LEFT);
                if (filter.getSearchIncludeSubCategories() && !filter.getChildCategoryIds().isEmpty()) {
                    predicates.add(cat.get("id").in(filter.getChildCategoryIds()));
                } else {
                    predicates.add(cb.equal(cat.get("id"), filter.getCategoryId()));
                }
            }
            if (filter.getBrandId() != null) {
                Join<Product, Brand> brand = root.join("brand", JoinType.LEFT);
                predicates.add(cb.equal(brand.get("id"), filter.getBrandId()));
            }
            if (filter.getProductTypeId() != null) {
                Join<Product, ProductType> productType = root.join("productType", JoinType.LEFT);
                predicates.add(cb.equal(productType.get("id"), filter.getProductTypeId()));
            }
            if (filter.getManufacturerId() != null) {
                Join<Product, Manufacturer> manufacturer = root.join("manufacturer", JoinType.LEFT);
                predicates.add(cb.equal(manufacturer.get("id"), filter.getManufacturerId()));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
