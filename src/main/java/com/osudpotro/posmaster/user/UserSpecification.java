package com.osudpotro.posmaster.user;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<User> filter(UserFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"));
            }
            if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("email")),
                        "%" + filter.getEmail().toLowerCase() + "%"));
            }
            if (filter.getMobile() != null && !filter.getMobile().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("mobile")),
                        "%" + filter.getMobile().toLowerCase() + "%"));
            }
            if (filter.getSecondaryEmail() != null && !filter.getSecondaryEmail().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("secondaryEmail")),
                        "%" + filter.getSecondaryEmail().toLowerCase() + "%"));
            }
            if (filter.getSecondaryMobile() != null && !filter.getSecondaryMobile().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("secondaryMobile")),
                        "%" + filter.getSecondaryMobile().toLowerCase() + "%"));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    public static Specification<User> filterOr(UserFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> orPredicates = new ArrayList<>();
            List<Predicate> andPredicates = new ArrayList<>();
            List<Predicate> predicates = new ArrayList<>();
            // -------- OR fields (search fields) --------
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                orPredicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"));
            }
            if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
                orPredicates.add(cb.like(cb.lower(root.get("email")),
                        "%" + filter.getEmail().toLowerCase() + "%"));
            }
            if (filter.getMobile() != null && !filter.getMobile().isEmpty()) {
                orPredicates.add(cb.like(cb.lower(root.get("mobile")),
                        "%" + filter.getMobile().toLowerCase() + "%"));
            }
            if (filter.getSecondaryEmail() != null && !filter.getSecondaryEmail().isEmpty()) {
                orPredicates.add(cb.like(cb.lower(root.get("secondaryEmail")),
                        "%" + filter.getSecondaryEmail().toLowerCase() + "%"));
            }
            if (filter.getSecondaryMobile() != null && !filter.getSecondaryMobile().isEmpty()) {
                orPredicates.add(cb.like(cb.lower(root.get("secondaryMobile")),
                        "%" + filter.getSecondaryMobile().toLowerCase() + "%"));
            }
            // -------- AND fields (strict filters) --------
            if (filter.getStatus() != null) {
                andPredicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            // -------- Combine safely --------
            Predicate finalPredicate = cb.conjunction(); // TRUE
            if (!orPredicates.isEmpty()) {
                finalPredicate = cb.and(
                        finalPredicate,
                        cb.or(orPredicates.toArray(new Predicate[0]))
                );
            }
            if (!andPredicates.isEmpty()) {
                finalPredicate = cb.and(
                        finalPredicate,
                        cb.and(andPredicates.toArray(new Predicate[0]))
                );
            }
            return finalPredicate;
//            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
