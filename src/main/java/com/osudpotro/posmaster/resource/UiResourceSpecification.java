package com.osudpotro.posmaster.resource;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UiResourceSpecification {
    public static Specification<UiResource> filter(UiResourceFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"));
            }
            if (filter.getUiResourceKey() != null && !filter.getUiResourceKey().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("uiResourceKey")),
                        "%" + filter.getUiResourceKey().toLowerCase() + "%"));
            }
            if (filter.getPageUrl() != null && !filter.getPageUrl().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("pageUrl")),
                        "%" + filter.getPageUrl().toLowerCase() + "%"));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
