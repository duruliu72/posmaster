package com.osudpotro.posmaster.branch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BranchRepository extends JpaSpecificationExecutor<Branch>,JpaRepository<Branch,Long> {
    boolean existsByName(String name);
    @Transactional
    @Modifying
    @Query("update Branch b set b.status = :status where b.id in :ids")
    int deleteBulkBranch(@Param("ids") List<Long> ids, @Param("status") Long status);
}
