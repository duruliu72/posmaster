package com.osudpotro.posmaster.sale;

import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.customer.Customer;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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
            Join<Sale, Customer> customer = root.join("customer", JoinType.INNER);

            if (filter.getCustomerName() != null && !filter.getCustomerName().isEmpty()) {
                predicates.add(cb.like(cb.lower(customer.get("userName")),
                        "%" + filter.getCustomerName().toLowerCase() + "%"));
            }
            if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
                predicates.add(cb.like(cb.lower(customer.get("email")),
                        "%" + filter.getEmail().toLowerCase() + "%"));
            }
            if (filter.getMobile() != null && !filter.getMobile().isEmpty()) {
                predicates.add(cb.like(cb.lower(customer.get("mobile")),
                        "%" + filter.getMobile().toLowerCase() + "%"));
            }

            if (filter.getSaleStatus() != null) {
                predicates.add(cb.equal(root.get("saleStatus"), filter.getSaleStatus()));
            }
            if (filter.getPaymentStatus() != null) {
                predicates.add(cb.equal(root.get("paymentStatus"), filter.getPaymentStatus()));
            }
            predicates.add(cb.equal(root.get("saleChannel"), 1));
            // Filter by branch of current user
            if (user != null && user.getBranch() != null) {
                predicates.add(cb.equal(root.get("branch").get("id"), user.getBranch().getId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}