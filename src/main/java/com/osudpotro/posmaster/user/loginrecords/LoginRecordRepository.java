package com.osudpotro.posmaster.user.loginrecords;

import com.osudpotro.posmaster.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoginRecordRepository extends JpaRepository<LoginRecord, Long> {

    // Find active session for a user
    Optional<LoginRecord> findTopByUserAndIsActiveTrueOrderByLoginTimeDesc(User user);

    // Find all active sessions
    List<LoginRecord> findByIsActiveTrue();

    // Find user's login history
    List<LoginRecord> findByUserOrderByLoginTimeDesc(User user);

    // Find user's login history with pagination
    Page<LoginRecord> findByUserId(Long userId, Pageable pageable);

    // Find active sessions with pagination
    Page<LoginRecord> findByIsActiveTrue(Pageable pageable);

    // Count active sessions for a user
    Long countByUserAndIsActiveTrue(User user);

    // Find login records by date range
    List<LoginRecord> findByLoginTimeBetween(LocalDateTime start, LocalDateTime end);

    // Find login records by device type
    List<LoginRecord> findByDeviceType(String deviceType);

    // Get online users count
    @Query("SELECT COUNT(DISTINCT lr.user) FROM LoginRecord lr WHERE lr.isActive = true")
    Long countOnlineUsers();

    // Get login statistics by date
    @Query("SELECT DATE(lr.loginTime), COUNT(lr) FROM LoginRecord lr " +
            "WHERE lr.loginTime BETWEEN :start AND :end GROUP BY DATE(lr.loginTime)")
    List<Object[]> getLoginStats(@Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);

    // Get top locations
    @Query("SELECT lr.country, COUNT(lr) FROM LoginRecord lr " +
            "GROUP BY lr.country ORDER BY COUNT(lr) DESC")
    List<Object[]> getTopLocations();
}