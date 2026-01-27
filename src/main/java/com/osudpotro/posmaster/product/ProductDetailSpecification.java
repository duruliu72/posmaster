package com.osudpotro.posmaster.product;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailSpecification {
    public static Specification<ProductDetail> filter(ProductFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getProductCode() != null && !filter.getProductCode().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("productDetailCode")),
                        "%" + filter.getProductCode().toLowerCase() + "%"));
            }
            if (filter.getProductBarCode() != null && !filter.getProductBarCode().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("productDetailBarCode")),
                        "%" + filter.getProductBarCode().toLowerCase() + "%"));
            }
            if (filter.getProductSku() != null && !filter.getProductSku().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("productDetailSku")),
                        "%" + filter.getProductSku().toLowerCase() + "%"));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
