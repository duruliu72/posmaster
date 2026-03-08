//package com.osudpotro.posmaster.user.loginrecords;
//
//import com.osudpotro.posmaster.user.User;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//@Repository
//public interface LoginRecordRepository extends JpaRepository<LoginRecord, Long> {
//
//    // Find active session for logout
//    @Query("SELECT l FROM LoginRecord l WHERE l.user.id = :userId AND l.isActive = true ORDER BY l.loginTime DESC")
//    Optional<LoginRecord> findActiveSessionByUserId(@Param("userId") Long userId);
//
//    // For login history
//    Page<LoginRecord> findByUserOrderByLoginTimeDesc(User user, Pageable pageable);
//
//    // Direct update method (as backup)
//    @Modifying
//    @Transactional
//    @Query("UPDATE LoginRecord l SET l.logoutTime = :logoutTime, l.isActive = false, l.sessionDurationSeconds = :duration WHERE l.user.id = :userId AND l.isActive = true")
//    int logoutUser(@Param("userId") Long userId,
//                   @Param("logoutTime") LocalDateTime logoutTime,
//                   @Param("duration") Long duration);
//}


package com.osudpotro.posmaster.user.loginrecords;

import com.osudpotro.posmaster.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface LoginRecordRepository extends JpaRepository<LoginRecord, Long> {

    // Find active session by userId
    @Query("SELECT l FROM LoginRecord l WHERE l.user.id = :userId AND l.isActive = true ORDER BY l.loginTime DESC")
    Optional<LoginRecord> findActiveSessionByUserId(@Param("userId") Long userId);
    // For login history
    Page<LoginRecord> findByUserOrderByLoginTimeDesc(User user, Pageable pageable);
    // Direct update method (backup)
    @Modifying
    @Transactional
    @Query("UPDATE LoginRecord l SET l.logoutTime = :logoutTime, l.isActive = false, l.sessionDurationSeconds = :duration WHERE l.user.id = :userId AND l.isActive = true")
    int logoutUser(@Param("userId") Long userId,
                   @Param("logoutTime") LocalDateTime logoutTime,
                   @Param("duration") Long duration);


}