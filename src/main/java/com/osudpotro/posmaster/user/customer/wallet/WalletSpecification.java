package com.osudpotro.posmaster.user.customer.wallet;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class WalletSpecification {
    public static Specification<Wallet> filter(WalletFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getNote() != null && !filter.getNote().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("note")),
                        "%" + filter.getNote().toLowerCase() + "%"));
            }

            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
