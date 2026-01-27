package com.osudpotro.posmaster.purchase.requisition;

import com.osudpotro.posmaster.purchase.PurchaseType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PurchaseRequisitionSpecification {
    public static Specification<PurchaseRequisition> filter(PurchaseRequisitionFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getRequsitionRef() != null && !filter.getRequsitionRef().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("requsitionRef")),
                        "%" + filter.getRequsitionRef().toLowerCase() + "%"));
            }
//            if (filter.getPurchaseType() != null && !filter.getPurchaseType().isEmpty()) {
//                PurchaseType.fromCode(filter.getPurchaseType());
//                predicates.add(cb.like(cb.lower(root.get("purchaseType")),
//                        "%" + filter.getRequsitionRef().toLowerCase() + "%"));
//            }
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
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
