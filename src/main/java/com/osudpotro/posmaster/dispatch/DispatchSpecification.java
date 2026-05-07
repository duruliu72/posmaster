package com.osudpotro.posmaster.dispatch;

import com.osudpotro.posmaster.purchase.PurchaseType;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisition;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DispatchSpecification {
    public static Specification<Dispatch> filter(DispatchFilter filter, User user) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();


//            if (user != null && user.getBranch() != null) {
//                predicates.add(cb.equal(root.get("branch").get("id"), user.getBranch().getId()));
//            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Dispatch> filterByRequesterBranch(DispatchFilter filter, User user) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (user != null && user.getBranch() != null) {
                predicates.add(cb.equal(root.get("requesterBranch").get("id"), user.getBranch().getId()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Dispatch> filterByAcceptorBranch(DispatchFilter filter, User user) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (user != null && user.getBranch() != null) {
                predicates.add(cb.equal(root.get("acceptorBranch").get("id"), user.getBranch().getId()));
            }
            predicates.add(root.get("dispatchStatus").in(2,3, 4,5));
            predicates.add(cb.isNotEmpty(root.get("items")));
//            predicates.add(cb.greaterThan(cb.size(root.get("items")), 0));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
