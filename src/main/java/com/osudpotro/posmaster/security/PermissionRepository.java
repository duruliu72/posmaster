package com.osudpotro.posmaster.security;

import com.osudpotro.posmaster.role.Role;
import com.osudpotro.posmaster.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    // Finds permissions either assigned to any of the roles OR to the specific user
    List<Permission> findByRoleInOrUser(Set<Role> roles, User user);
    @Transactional
    @Modifying
    @Query("UPDATE Permission p SET p.isResourceChecked = :isResourceChecked WHERE p.apiResource.id NOT IN :ids AND p.role.id= :roleId")
    void updatePermissionByRoleAndResources(@Param("isResourceChecked") boolean isResourceChecked, @Param("ids") List<Long> ids, @Param("roleId") Long roleId);
    @Query("SELECT p FROM Permission p WHERE p.apiResource.id NOT IN :ids AND p.role.id= :roleId")
    List<Permission> findPermissionByRoleAndResources(@Param("ids") List<Long> ids, @Param("roleId") Long roleId);
}