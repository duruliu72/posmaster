package com.osudpotro.posmaster.offerhub.membership;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaSpecificationExecutor<Membership>,JpaRepository<Membership,Long> {
    boolean existsByName(String name);
    Optional<Membership> findByCode(String code);
    @Transactional
    @Modifying
    @Query("update Membership b set b.status = :status where b.id in :ids")
    int deleteBulkEntity(@Param("ids") List<Long> ids, @Param("status") Long status);
}