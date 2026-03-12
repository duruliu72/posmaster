package com.osudpotro.posmaster.purchase.transfer;

import com.osudpotro.posmaster.purchase.PurchaseType;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PurchaseRequisitionTransferSpecification {
    public static Specification<PurchaseRequisitionTransfer> filterByPurchaseRequisition(PurchaseRequisitionTransferFilter filter, Long purchaseRequisitionId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (purchaseRequisitionId != null) {
                predicates.add(cb.equal(
                        root.get("purchaseRequisition").get("id"),
                        purchaseRequisitionId
                ));
            }
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
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<PurchaseRequisitionTransfer> filter(PurchaseRequisitionTransferFilter filter) {
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
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<PurchaseRequisitionTransfer> filterByDelivered(PurchaseRequisitionTransferFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getRequsitionRef() != null && !filter.getRequsitionRef().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("requsitionRef")),
                        "%" + filter.getRequsitionRef().toLowerCase() + "%"));
            }
            if (filter.getIsReceived() != null) {
                predicates.add(cb.equal(root.get("goodsOnTrip").get("isReceived"), filter.getIsReceived()));
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
            predicates.add(cb.equal(root.get("goodsOnTrip").get("goodsStatus"), GoodsStatus.DELIVERED));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
