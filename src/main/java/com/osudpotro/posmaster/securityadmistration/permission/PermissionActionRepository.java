package com.osudpotro.posmaster.securityadmistration.permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PermissionActionRepository extends JpaRepository<PermissionAction, Long> {
    @Query("SELECT pa FROM PermissionAction pa WHERE pa.permission.id = :permissionId AND pa.action.id=:actionId")
    Optional<PermissionAction> findByPermissionIdAndActionId(@Param("permissionId") Long permissionId, @Param("actionId") Long actionId);
    @Transactional
    @Modifying
    @Query("UPDATE PermissionAction pd SET pd.isActive = :isActionChecked WHERE pd.permission.id IN :ids")
    void updatePermissionDetailByPermissions(@Param("isActionChecked") boolean isActionChecked, @Param("ids") List<Long> ids);

    @Query(value = """
            SELECT 
                ra.*,
                COALESCE(pa.is_active, false) AS is_active,
                a.name AS actionName
            FROM resource_actions ra
            LEFT JOIN permissions p 
                ON p.resource_id = ra.resource_id
                AND p.role_id = :roleId
            LEFT JOIN permission_actions pa 
                ON pa.permission_id = p.id
                AND pa.action_id = ra.action_id
            JOIN actions a 
                ON a.id = ra.action_id
            WHERE ra.resource_id = :resourceId 
                AND ra.checked = true
            ORDER BY a.id,ra.id
            """, nativeQuery = true)
    List<PermissionActionProjection> getPermissionActions(
            @Param("roleId") Long roleId,
            @Param("resourceId") Long resourceId
    );
}
