package com.osudpotro.posmaster.user.admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface AdminUserRepository extends JpaSpecificationExecutor<AdminUser>, JpaRepository<AdminUser, Long> {
    Optional<AdminUser> findByEmail(String email);
    Optional<AdminUser> findByMobile(String mobile);
    Optional<AdminUser> findByEmailOrMobile(String email,String mobile);
    boolean existsByEmail(String email);
    boolean existsByMobile(String mobile);
}
