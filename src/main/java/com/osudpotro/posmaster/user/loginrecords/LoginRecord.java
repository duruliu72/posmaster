package com.osudpotro.posmaster.user.loginrecords;

import com.osudpotro.posmaster.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "login_records",
        indexes = {
                @Index(name = "idx_login_user_id", columnList = "user_id"),
                @Index(name = "idx_login_time", columnList = "login_time"),
                @Index(name = "idx_login_status", columnList = "status"),
                @Index(name = "idx_is_active", columnList = "is_active")
        })
public class LoginRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "login_time", nullable = false)
    private LocalDateTime loginTime;

    @Column(name = "logout_time")
    private LocalDateTime logoutTime;

    @Column(name = "auto_logout_time")
    private Boolean autoLoggedOut = false;

    @Column(name = "session_duration_seconds")
    private Long sessionDurationSeconds;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "location")
    private String location;

    @Column(name = "device_type")
    private String deviceType; // Mobile, Desktop, Tablet

    @Column(name = "device_brand")
    private String deviceBrand; // Apple, Samsung, Xiaomi, etc.

    @Column(name = "device_model")  // ← ADD THIS NEW FIELD
    private String deviceModel;      // iPhone 14 Pro, SM-G991B, etc.

    @Column(name = "os_name")
    private String osName;

    @Column(name = "browser_name")
    private String browserName;

    @Column(name = "login_method")
    private String loginMethod;

    @Column(name = "login_success")
    private Boolean loginSuccess = true;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "status")
    private Integer status = 1;
}