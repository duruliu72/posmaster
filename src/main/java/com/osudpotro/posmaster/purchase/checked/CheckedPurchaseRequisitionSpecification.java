package com.osudpotro.posmaster.purchase.checked;

import com.osudpotro.posmaster.purchase.PurchaseType;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CheckedPurchaseRequisitionSpecification {
    public static Specification<CheckedPurchaseRequisition> filter(CheckedPurchaseRequisitionFilter filter, User user) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getRequsitionRef() != null && !filter.getRequsitionRef().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("requsitionRef")),
                        "%" + filter.getRequsitionRef().toLowerCase() + "%"));
            }
            if (filter.getPurchaseType() != null) {
                PurchaseType purchaseType =
                        PurchaseType.fromCode(filter.getPurchaseType());

                predicates.add(
                        cb.equal(root.get("purchaseRequisition").get("purchaseType"), purchaseType)
                );
            }
            if (filter.getCheckedStatus() != null) {
                predicates.add(cb.equal(root.get("checkedStatus"), 2));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    public static Specification<CheckedPurchaseRequisition> filterByBranch(CheckedPurchaseRequisitionFilter filter, User user) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getRequsitionRef() != null && !filter.getRequsitionRef().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("requsitionRef")),
                        "%" + filter.getRequsitionRef().toLowerCase() + "%"));
            }
            if (filter.getPurchaseType() != null) {
                PurchaseType purchaseType =
                        PurchaseType.fromCode(filter.getPurchaseType());
                predicates.add(
                        cb.equal(root.get("purchaseRequisition").get("purchaseType"), purchaseType)
                );
            }
            if (filter.getCheckedStatus() != null) {
                predicates.add(cb.equal(root.get("checkedStatus"), filter.getCheckedStatus()));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            if (user != null) {
                Long branchId=user.getBranch().getId();
                predicates.add(cb.equal(root.get("purchaseRequisition").get("branch").get("id"),branchId ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
