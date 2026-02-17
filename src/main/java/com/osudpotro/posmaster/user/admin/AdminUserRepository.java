package com.osudpotro.posmaster.user.admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AdminUserRepository extends JpaSpecificationExecutor<AdminUser>, JpaRepository<AdminUser, Long> {
    @Transactional
    @Modifying
    @Query("update AdminUser c set c.status = :status where c.id in :ids")
    int deleteBulkAdminUser(@Param("ids") List<Long> ids, @Param("status") Long status);
}
