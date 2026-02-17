package com.osudpotro.posmaster.user.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaSpecificationExecutor<Employee>, JpaRepository<Employee, Long> {
    @Transactional
    @Modifying
    @Query("update Employee c set c.status = :status where c.id in :ids")
    int deleteBulkEmployee(@Param("ids") List<Long> ids, @Param("status") Long status);
}
