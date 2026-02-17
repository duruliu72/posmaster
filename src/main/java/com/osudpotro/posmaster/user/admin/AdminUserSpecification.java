package com.osudpotro.posmaster.user.admin;

import com.osudpotro.posmaster.user.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AdminUserSpecification {
    public static Specification<AdminUser> filter(AdminUserFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<AdminUser, User> user = root.join("user", JoinType.INNER);
            if (filter.getUserName() != null && !filter.getUserName().isEmpty()) {
                predicates.add(cb.like(cb.lower(user.get("userName")),
                        "%" + filter.getUserName().toLowerCase() + "%"));
            }
            if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
                predicates.add(cb.like(cb.lower(user.get("email")),
                        "%" + filter.getEmail().toLowerCase() + "%"));
            }
            if (filter.getFirstName() != null && !filter.getFirstName().isEmpty()) {
                predicates.add(cb.like(cb.lower(user.get("firstName")),
                        "%" + filter.getFirstName().toLowerCase() + "%"));
            }
            if (filter.getLastName() != null && !filter.getLastName().isEmpty()) {
                predicates.add(cb.like(cb.lower(user.get("lastName")),
                        "%" + filter.getLastName().toLowerCase() + "%"));
            }
            if (filter.getMobile() != null && !filter.getMobile().isEmpty()) {
                predicates.add(cb.like(cb.lower(user.get("mobile")),
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
}
