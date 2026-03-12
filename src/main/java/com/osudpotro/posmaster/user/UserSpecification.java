package com.osudpotro.posmaster.user;

import com.osudpotro.posmaster.role.Role;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<User> filter(UserFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getUserName() != null && !filter.getUserName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("userName")),
                        "%" + filter.getUserName().toLowerCase() + "%"));
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
            if (filter.getUserName() != null && !filter.getUserName().isEmpty()) {
                orPredicates.add(cb.like(cb.lower(root.get("userName")),
                        "%" + filter.getUserName().toLowerCase() + "%"));
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
            Join<User, Role> roleJoin = root.join("roles");
            andPredicates.add(
                    roleJoin.get("roleKey").in("ROLE_SUPER_ADMIN","ROLE_ADMIN", "ROLE_EMPLOYEE")
            );
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
