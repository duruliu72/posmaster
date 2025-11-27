package com.osudpotro.posmaster.producttype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductTypeRepository extends JpaSpecificationExecutor<ProductType>,JpaRepository<ProductType,Long> {
    boolean existsByName(String name);
    @Transactional
    @Modifying
    @Query("update ProductType pt set pt.status = :status where pt.id in :ids")
    int deleteBulkProductType(@Param("ids") List<Long> ids, @Param("status") Long status);
}