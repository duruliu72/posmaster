package com.osudpotro.posmaster.dispatch;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DispatchItemSpecification {
    public static Specification<DispatchItem> filter(DispatchItemFilter filter, Long dispatchId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("product").get("productName")),
                        "%" + filter.getName().toLowerCase() + "%"));
            }
            if (dispatchId != null) {
                predicates.add(cb.equal(root.get("dispatch").get("id"), dispatchId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
