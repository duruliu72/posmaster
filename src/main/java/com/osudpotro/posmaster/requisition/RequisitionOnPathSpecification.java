package com.osudpotro.posmaster.requisition;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RequisitionOnPathSpecification {
    public static Specification<RequisitionOnPath> filter(RequisitionOnPathFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getRequsitionRef() != null && !filter.getRequsitionRef().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("requisition").get("requsitionRef")),
                        "%" + filter.getRequsitionRef().toLowerCase() + "%"));
            }
            if (filter.getRequisitionType() != null) {
                predicates.add(cb.equal(root.get("requisition").get("requisitionType").get("id"), filter.getRequisitionType()));
            }
            if (filter.getRequisitionStatus() != null) {
                predicates.add(cb.equal(root.get("requisition").get("requisitionStatus"), filter.getRequisitionStatus()));
            }
            if (filter.getApprovedStatus() != null) {
                predicates.add(cb.equal(root.get("approvedStatus"), filter.getApprovedStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<RequisitionOnPath> filterByUser(RequisitionOnPathFilter filter, Long userId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // JOIN requisition
            Join<RequisitionOnPath, Requisition> requisition =
                    root.join("requisition", JoinType.INNER);
            // rop.reviewCount = r.reviewCount
            predicates.add(
                    cb.equal(
                            root.get("reviewCount"),
                            requisition.get("reviewCount")
                    )
            );
            if (userId != null) {
                predicates.add(
                        cb.equal(root.get("user").get("id"), userId)
                );
            }
            if (filter.getRequsitionRef() != null && !filter.getRequsitionRef().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("requisition").get("requsitionRef")),
                        "%" + filter.getRequsitionRef().toLowerCase() + "%"));
            }
            if (filter.getRequisitionType() != null) {
                predicates.add(cb.equal(root.get("requisition").get("requisitionType").get("id"), filter.getRequisitionType()));
            }
            if (filter.getRequisitionStatus() != null) {
                predicates.add(cb.equal(root.get("requisition").get("requisitionStatus"), filter.getRequisitionStatus()));
            }
            if (filter.getApprovedStatus() != null) {
                predicates.add(cb.equal(root.get("approvedStatus"), filter.getApprovedStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
