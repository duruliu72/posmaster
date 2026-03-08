package com.osudpotro.posmaster.user.customer.profile;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CustomerLoginDeviceResponse {
    private Long id;
    private String deviceType;
    private String deviceBrand;
    private String deviceModel;
    private String osName;
    private String browserName;
    private String location;
    private String country;
    private String city;
    private String ipAddress;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private Boolean isActive;
    private Long sessionDurationSeconds;
    private String loginMethod;
}