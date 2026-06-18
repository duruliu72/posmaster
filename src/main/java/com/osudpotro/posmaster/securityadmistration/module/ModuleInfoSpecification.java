package com.osudpotro.posmaster.securityadmistration.module;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ModuleInfoSpecification {
    public static Specification<ModuleInfo> filter(ModuleFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getModuleName() != null && !filter.getModuleName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("moduleName")),
                        "%" + filter.getModuleName().toLowerCase() + "%"));
            }
            if (filter.getModuleCode()!= null && !filter.getModuleCode().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("moduleCode")),
                        "%" + filter.getModuleCode().toLowerCase() + "%"));
            }

            if (filter.getDescription() != null && !filter.getDescription().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("description")),
                        "%" + filter.getDescription().toLowerCase() + "%"));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
