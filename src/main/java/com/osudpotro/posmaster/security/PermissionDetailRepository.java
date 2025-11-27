package com.osudpotro.posmaster.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PermissionDetailRepository extends JpaRepository<PermissionDetail, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE PermissionDetail pd SET pd.isActionChecked = :isActionChecked WHERE pd.permission.id IN :ids")
    void updatePermissionDetailByPermissions(@Param("isActionChecked") boolean isActionChecked, @Param("ids") List<Long> ids);
}
