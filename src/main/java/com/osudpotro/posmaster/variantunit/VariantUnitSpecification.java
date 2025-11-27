package com.osudpotro.posmaster.variantunit;


import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class VariantUnitSpecification {
    public static Specification<VariantUnit> filter(VariantUnitFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            if (filter.getVariantTypeId() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getVariantTypeId()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
