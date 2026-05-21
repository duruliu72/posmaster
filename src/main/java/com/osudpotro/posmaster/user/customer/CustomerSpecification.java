package com.osudpotro.posmaster.user.customer;

import com.osudpotro.posmaster.user.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {
    public static Specification<Customer> filter(CustomerFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Customer, User> user = root.join("user", JoinType.INNER);
            if (filter.getUserName() != null && !filter.getUserName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("userName")),
                        "%" + filter.getUserName().toLowerCase() + "%"));
            }
            if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("email")),
                        "%" + filter.getEmail().toLowerCase() + "%"));
            }
            if (filter.getFirstName() != null && !filter.getFirstName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("firstName")),
                        "%" + filter.getFirstName().toLowerCase() + "%"));
            }
            if (filter.getLastName() != null && !filter.getLastName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("lastName")),
                        "%" + filter.getLastName().toLowerCase() + "%"));
            }
            if (filter.getMobile() != null && !filter.getMobile().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("mobile")),
                        "%" + filter.getMobile().toLowerCase() + "%"));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            if (filter.getCreatedFrom() != null && filter.getCreatedTo() != null) {
                predicates.add(cb.between(root.get("createdAt"),
                        filter.getCreatedFrom(), filter.getCreatedTo()));
            } else if (filter.getCreatedFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedFrom()));
            } else if (filter.getCreatedTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getCreatedTo()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Customer> orOpFilter(CustomerFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> searchPredicates = new ArrayList<>();
            Join<Customer, User> user = root.join("user", JoinType.INNER);
            if (filter.getUserName() != null && !filter.getUserName().isEmpty()) {
                searchPredicates.add(cb.like(cb.lower(root.get("userName")),
                        "%" + filter.getUserName().toLowerCase() + "%"));
            }
            if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
                searchPredicates.add(cb.like(cb.lower(root.get("email")),
                        "%" + filter.getEmail().toLowerCase() + "%"));
            }
            if (filter.getMobile() != null && !filter.getMobile().isEmpty()) {
                searchPredicates.add(cb.like(cb.lower(root.get("mobile")),
                        "%" + filter.getMobile().toLowerCase() + "%"));
            }
            if (filter.getFirstName() != null && !filter.getFirstName().isEmpty()) {
                searchPredicates.add(cb.like(cb.lower(root.get("firstName")),
                        "%" + filter.getFirstName().toLowerCase() + "%"));
            }
            if (filter.getLastName() != null && !filter.getLastName().isEmpty()) {
                searchPredicates.add(cb.like(cb.lower(root.get("lastName")),
                        "%" + filter.getLastName().toLowerCase() + "%"));
            }
            List<Predicate> finalPredicates = new ArrayList<>();
            if (!searchPredicates.isEmpty()) {
                finalPredicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
            }
            if (filter.getStatus() != null) {
                finalPredicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            if (finalPredicates.isEmpty()) {
                return cb.conjunction();
            }
            return cb.and(finalPredicates.toArray(new Predicate[0]));
        };
    }
}
