package com.osudpotro.posmaster.user.loginrecords;

import com.osudpotro.posmaster.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "login_records", indexes = {
        @Index(name = "idx_login_user_id", columnList = "user_id"),
        @Index(name = "idx_login_time", columnList = "login_time"),
        @Index(name = "idx_login_status", columnList = "status")
})
public class LoginRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "login_time")
    private LocalDateTime loginTime;

    @Column(name = "logout_time")
    private LocalDateTime logoutTime;

    @Column(name = "session_duration")
    private Long sessionDuration;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "country")
    private String country;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "city")
    private String city;

    @Column(name = "region")
    private String region;

    @Column(name = "region_code")
    private String regionCode;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "time_zone")        // ✅ This is the missing field!
    private String timeZone;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "accuracy_radius")
    private Integer accuracyRadius;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "device_brand")
    private String deviceBrand;

    @Column(name = "device_model")
    private String deviceModel;

    @Column(name = "os_name")
    private String osName;

    @Column(name = "os_version")
    private String osVersion;

    @Column(name = "browser_name")
    private String browserName;

    @Column(name = "browser_version")
    private String browserVersion;

    @Column(name = "browser_engine")
    private String browserEngine;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "screen_resolution")
    private String screenResolution;

    @Column(name = "accept_language")
    private String acceptLanguage;

    @Column(name = "accept_encoding")
    private String acceptEncoding;

    @Column(name = "connection_type")
    private String connectionType;

    @Column(name = "client_timezone")
    private String clientTimezone;

    @Column(name = "isp")
    private String isp;

    @Column(name = "organization")
    private String organization;

    @Column(name = "network_type")
    private String networkType;

    @Column(name = "protocol")
    private String protocol;

    @Column(name = "method")
    private String method;

    @Column(name = "server_name")
    private String serverName;

    @Column(name = "server_port")
    private Integer serverPort;

    @Column(name = "request_uri")
    private String requestUri;

    @Column(name = "query_string")
    private String queryString;

    @Column(name = "login_method")
    private String loginMethod;

    @Column(name = "status")
    private Integer status = 1;
}