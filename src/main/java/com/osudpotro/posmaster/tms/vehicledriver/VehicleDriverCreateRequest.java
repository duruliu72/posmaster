package com.osudpotro.posmaster.tms.vehicledriver;

import lombok.Data;

@Data
public class VehicleDriverCreateRequest {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String password;
    private String secondaryEmail;
    private String secondaryMobile;
    private Integer gender;
    private String provider;
    private String providerId;
    private Long multimediaId;
}
