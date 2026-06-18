package com.osudpotro.posmaster.securityadmistration.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaSpecificationExecutor<Role>,JpaRepository<Role, Long> {
    boolean existsByRoleKey(String roleKey);
    Optional<Role> findByRoleKey(String roleKey);
    @Query("SELECT r FROM Role r WHERE r.id = :id")
    Optional<RoleProjectionDto> findRoleById(@Param("id") Long id);
}