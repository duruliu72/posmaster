package com.osudpotro.posmaster.inventory;

import com.osudpotro.posmaster.category.Category;
import com.osudpotro.posmaster.product.Product;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class InventorySpecification {
    public static Specification<Inventory> filter(InventoryFilter filter, User user) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getPurchaseBatchNo() != null && !filter.getPurchaseBatchNo().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("purchaseBatchNo")),
                        "%" + filter.getPurchaseBatchNo().toLowerCase() + "%"));
            }
            if (filter.getProductionBatchNo() != null && !filter.getProductionBatchNo().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("productionBatchNo")),
                        "%" + filter.getProductionBatchNo().toLowerCase() + "%"));
            }
            if (filter.getPurchaseBarCode() != null && !filter.getPurchaseBarCode().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("purchaseBarCode")),
                        "%" + filter.getPurchaseBarCode().toLowerCase() + "%"));
            }
            if (filter.getProductName() != null && !filter.getProductName().isEmpty()) {
                Join<Inventory, Product> product = root.join("product", JoinType.INNER);
                predicates.add(cb.like(cb.lower(product.get("productName")),
                        "%" + filter.getProductName().toLowerCase() + "%"));
            }
            if (user != null &&user.getBranch()!=null) {
                predicates.add(cb.equal(root.get("branch").get("id"), user.getBranch().getId()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
