package com.osudpotro.posmaster.varianttype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VariantTypeRepository extends JpaSpecificationExecutor<VariantType>,JpaRepository<VariantType,Long> {
    boolean existsByName(String name);
    @Transactional
    @Modifying
    @Query("update VariantType vt set vt.status = :status where vt.id in :ids")
    int deleteBulkVariantType(@Param("ids") List<Long> ids, @Param("status") Long status);
}
