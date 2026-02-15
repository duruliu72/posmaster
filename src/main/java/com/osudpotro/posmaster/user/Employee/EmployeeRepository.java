package com.osudpotro.posmaster.user.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EmployeeRepository extends JpaSpecificationExecutor<Employee>, JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByMobile(String mobile);
    Optional<Employee> findByEmailOrMobile(String email,String mobile);
    boolean existsByEmail(String email);
    boolean existsByMobile(String mobile);
}
