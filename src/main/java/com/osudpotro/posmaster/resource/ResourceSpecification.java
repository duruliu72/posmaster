package com.osudpotro.posmaster.resource;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ResourceSpecification {
    public static Specification<Resource> filter(ResourceFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"));
            }
            if (filter.getResourceKey() != null && !filter.getResourceKey().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("uiResourceKey")),
                        "%" + filter.getResourceKey().toLowerCase() + "%"));
            }
            if (filter.getUrl() != null && !filter.getUrl().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("pageUrl")),
                        "%" + filter.getUrl().toLowerCase() + "%"));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
