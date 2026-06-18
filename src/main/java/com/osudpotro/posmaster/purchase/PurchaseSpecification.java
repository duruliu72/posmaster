package com.osudpotro.posmaster.purchase;

import com.osudpotro.posmaster.user.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PurchaseSpecification {
    public static Specification<Purchase> filter(PurchaseFilter filter, User user) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"));
            }
            if (user != null && user.getBranch() != null) {
                predicates.add(cb.equal(root.get("branch").get("id"), user.getBranch().getId()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
