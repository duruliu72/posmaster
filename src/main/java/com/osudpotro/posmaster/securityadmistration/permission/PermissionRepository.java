package com.osudpotro.posmaster.securityadmistration.permission;

import com.osudpotro.posmaster.securityadmistration.role.Role;
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
    @Query("UPDATE Permission p SET p.isResourceChecked = :isResourceChecked WHERE p.resource.id NOT IN :ids AND p.role.id= :roleId")
    void updatePermissionByRoleAndResources(@Param("isResourceChecked") boolean isResourceChecked, @Param("ids") List<Long> ids, @Param("roleId") Long roleId);

    @Query("SELECT p FROM Permission p WHERE p.resource.id NOT IN :ids AND p.role.id= :roleId")
    List<Permission> findPermissionByRoleAndResources(@Param("ids") List<Long> ids, @Param("roleId") Long roleId);
    @Query(value = """
            SELECT 
                p.id AS permissionId,
                r.name AS resourceName,
                r.module_id AS moduleId,
                mi.module_name as moduleName,
                r.id AS resourceId
            FROM resources r
            JOIN module_info mi on mi.id=r.module_id
            LEFT JOIN permissions p 
                ON p.resource_id = r.id 
                AND p.role_id = :roleId
            WHERE (:moduleId IS NULL OR r.module_id = :moduleId)
              AND r.url IS NOT NULL
              AND r.url <> '#' ORDER BY r.id
            """, nativeQuery = true)
    List<PermissionProjection> getRoleResources(
            @Param("roleId") Long roleId,
            @Param("moduleId") Long moduleId
    );
}