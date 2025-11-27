package com.osudpotro.posmaster.manufacturer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ManufacturerRepository extends JpaSpecificationExecutor<Manufacturer>,JpaRepository<Manufacturer,Long> {
    boolean existsByName(String name);
    @Transactional
    @Modifying
    @Query("update Manufacturer m set m.status = :status where m.id in :ids")
    int deleteBulkManufacturer(@Param("ids") List<Long> ids, @Param("status") Long status);
}