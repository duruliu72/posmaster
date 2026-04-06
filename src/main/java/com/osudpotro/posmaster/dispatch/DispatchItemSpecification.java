package com.osudpotro.posmaster.dispatch;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DispatchItemSpecification {
    public static Specification<DispatchItem> filter(DispatchItemFilter filter, Long dispatchId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (dispatchId != null) {
//                System.out.println(dispatchId);
//                predicates.add(cb.equal(root.get("dispatch.id"), dispatchId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
