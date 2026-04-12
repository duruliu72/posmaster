package com.osudpotro.posmaster.purchase.requisition;

import com.osudpotro.posmaster.purchase.PurchaseType;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PurchaseRequisitionSpecification {
    public static Specification<PurchaseRequisition> filter(PurchaseRequisitionFilter filter, User user) {
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
                        cb.equal(root.get("purchaseType"), purchaseType)
                );
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            if (user != null && user.getBranch() != null) {
                predicates.add(cb.equal(root.get("branch").get("id"), user.getBranch().getId()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
