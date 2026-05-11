package com.osudpotro.posmaster.sale;

import com.osudpotro.posmaster.user.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SaleSpecification {

    public static Specification<Sale> filter(SaleFilter filter, User user) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getSaleRef() != null && !filter.getSaleRef().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("saleRef")),
                        "%" + filter.getSaleRef().toLowerCase() + "%"));
            }

            if (filter.getSaleStatus() != null) {
                predicates.add(cb.equal(root.get("saleStatus"), filter.getSaleStatus()));
            }

            if (filter.getPaymentStatus() != null) {
                predicates.add(cb.equal(root.get("paymentStatus"), filter.getPaymentStatus()));
            }

            // Filter by branch of current user
            if (user != null && user.getBranch() != null) {
                predicates.add(cb.equal(root.get("branch").get("id"), user.getBranch().getId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}