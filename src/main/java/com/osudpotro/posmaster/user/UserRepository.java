package com.osudpotro.posmaster.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaSpecificationExecutor<User>, JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByMobile(String mobile);
    @Query("""
        SELECT DISTINCT u FROM User u
        LEFT JOIN FETCH u.roles r
        LEFT JOIN FETCH r.permissions rp
        LEFT JOIN FETCH u.permissions up
        LEFT JOIN FETCH rp.permissionDetails rpd
        LEFT JOIN FETCH up.permissionDetails upd
        WHERE u.id = :id
        """)
    Optional<User> findUserWithAllPermissions(@Param("id") Long userId);
}
