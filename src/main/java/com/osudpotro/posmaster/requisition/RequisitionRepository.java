package com.osudpotro.posmaster.requisition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RequisitionRepository extends JpaSpecificationExecutor<Requisition>,JpaRepository<Requisition,Long> {

}
