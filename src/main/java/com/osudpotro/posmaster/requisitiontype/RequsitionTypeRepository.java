package com.osudpotro.posmaster.requisitiontype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RequsitionTypeRepository  extends JpaSpecificationExecutor<RequisitionType>, JpaRepository<RequisitionType,Long> {
    boolean existsByName(String name);
    Optional<RequisitionType> findByRequisitionTypeKey(String requisitionTypeKey);
}
