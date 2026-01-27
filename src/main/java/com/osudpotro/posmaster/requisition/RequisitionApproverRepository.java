package com.osudpotro.posmaster.requisition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RequisitionApproverRepository extends JpaRepository<RequisitionApprover, Long> {
    List<RequisitionApprover> findByRequisitionTypeIdOrderByIdAsc(Long requisitionTypeId);
    @Query("""
                SELECT ar FROM RequisitionApprover ar
                WHERE ar.requisitionType.id = :requsitionTypeId
                  AND ar.user.id = :userId
            """)
    Optional<RequisitionApprover> findRequisitionApprover(
            @Param("requsitionTypeId") Long requsitionTypeId,
            @Param("userId") Long userId
    );

    @Query("""
                SELECT ar FROM RequisitionApprover ar
                WHERE ar.requisitionType.id = :requsitionTypeId
                  AND ar.nextUser.id IS NULL
            """)
    Optional<RequisitionApprover> findApproverWithNullNextUser(@Param("requsitionTypeId") Long requsitionTypeId);

    @Query("""
                SELECT ar FROM RequisitionApprover ar
                WHERE ar.requisitionType.id = :requsitionTypeId
                  AND ar.prevUser.id IS NULL
            """)
    Optional<RequisitionApprover> findApproverWithNullPrevUser(@Param("requsitionTypeId") Long requsitionTypeId);
}
