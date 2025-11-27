package com.osudpotro.posmaster.requisitiontype;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequsitionTypeRepository  extends JpaRepository<RequisitionType,Long> {
    boolean existsByName(String name);
}
