package com.osudpotro.posmaster.requisition;

import com.osudpotro.posmaster.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RequisitionOnPathRepository extends JpaSpecificationExecutor<RequisitionOnPath>, JpaRepository<RequisitionOnPath, Long> {
    @Query("""
                SELECT rop FROM RequisitionOnPath rop
                JOIN rop.requisition r
                WHERE rop.reviewCount = r.reviewCount
                  AND rop.requisition.id = :requisitionId
                  AND rop.user.id = :userId
            """)
    List<RequisitionOnPath> findRequisitionOnPathByUser(@Param("userId") Long userId, @Param("requisitionId") Long requisitionId);

    @Query("""
                SELECT COUNT(rop) > 0 FROM RequisitionOnPath rop
                JOIN rop.requisition r
                WHERE rop.reviewCount = r.reviewCount
                  AND rop.requisition.id = :requisitionId
                  AND rop.user.id = :userId
            """)
    boolean existRequisitionOnPathByUser(@Param("userId") Long userId, @Param("requisitionId") Long requisitionId);

    List<RequisitionOnPath> findAllByUser(User user);

    Optional<RequisitionOnPath> findByIdAndUser(Long id, User user);

    @Query("""
                SELECT rop FROM RequisitionOnPath rop
                JOIN rop.requisition r
                WHERE rop.reviewCount = r.reviewCount
                  AND rop.user.id = :userId
            """)
    Page<RequisitionOnPath> findRequisitionOnPathByUserWithPage(@Param("userId") Long userId, Pageable pageable);
}
