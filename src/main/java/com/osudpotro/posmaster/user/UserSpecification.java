package com.osudpotro.posmaster.user;

import com.osudpotro.posmaster.role.Role;
import com.osudpotro.posmaster.user.Employee.Employee;
import com.osudpotro.posmaster.user.admin.AdminUser;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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
            Join<User, AdminUser> adminJoin = root.join("adminUser", JoinType.LEFT);
            Join<User, Employee> employeeJoin = root.join("employee", JoinType.LEFT);
            if (filter.getUserName() != null && !filter.getUserName().isEmpty()) {
                String searchPattern = "%" + filter.getUserName().toLowerCase() + "%";
                Predicate adminName = cb.like(cb.lower(adminJoin.get("userName")), searchPattern);
                Predicate employeeName = cb.like(cb.lower(employeeJoin.get("userName")), searchPattern);
                orPredicates.add(cb.or(adminName, employeeName));
            }
            if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
                String searchPattern = "%" + filter.getEmail().toLowerCase() + "%";
                Predicate adminEmail = cb.like(cb.lower(adminJoin.get("email")), searchPattern);
                Predicate employeeEmail = cb.like(cb.lower(employeeJoin.get("email")), searchPattern);
                orPredicates.add(cb.or(adminEmail, employeeEmail));
            }
            if (filter.getMobile() != null && !filter.getMobile().isEmpty()) {
                String searchPattern = "%" + filter.getMobile().toLowerCase() + "%";
                Predicate adminMobile = cb.like(cb.lower(adminJoin.get("mobile")), searchPattern);
                Predicate employeeMobile = cb.like(cb.lower(employeeJoin.get("mobile")), searchPattern);
                orPredicates.add(cb.or(adminMobile, employeeMobile));
            }
//            if (filter.getSecondaryEmail() != null && !filter.getSecondaryEmail().isEmpty()) {
//                String searchPattern = "%" + filter.getSecondaryEmail().toLowerCase() + "%";
//                Predicate adminSecondaryEmail = cb.like(cb.lower(adminJoin.get("secondaryEmail")), searchPattern);
//                Predicate employeeSecondaryEmail = cb.like(cb.lower(employeeJoin.get("secondaryEmail")), searchPattern);
//                orPredicates.add(cb.or(adminSecondaryEmail, employeeSecondaryEmail));
//            }
//            if (filter.getSecondaryMobile() != null && !filter.getSecondaryMobile().isEmpty()) {
//                String searchPattern = "%" + filter.getSecondaryMobile().toLowerCase() + "%";
//                Predicate adminSecondaryMobile = cb.like(cb.lower(adminJoin.get("secondaryMobile")), searchPattern);
//                Predicate employeeSecondaryMobile = cb.like(cb.lower(employeeJoin.get("secondaryMobile")), searchPattern);
//                orPredicates.add(cb.or(adminSecondaryMobile, employeeSecondaryMobile));
//            }
            // -------- AND fields (strict filters) --------
            if (filter.getStatus() != null) {
                andPredicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            Join<User, Role> roleJoin = root.join("roles");
            andPredicates.add(
                    roleJoin.get("roleKey").in("ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_EMPLOYEE")
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
