package com.osudpotro.posmaster.purchase.transfer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PurchaseRequisitionItemTransferSpecification {
    public static Specification<PurchaseRequisitionItemTransfer> filter(PurchaseRequisitionItemTransferFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getPurchaseRequisitionTransferId() != null) {
                predicates.add(cb.equal(
                        root.get("purchaseRequisitionTransfer").get("id"),
                        filter.getPurchaseRequisitionTransferId()
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
