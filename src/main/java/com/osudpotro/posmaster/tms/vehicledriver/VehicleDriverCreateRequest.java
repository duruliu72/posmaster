package com.osudpotro.posmaster.tms.vehicledriver;

import lombok.Data;

@Data
public class VehicleDriverCreateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String secondaryEmail;
    private String secondaryPhone;
    private Integer gender;
    private String provider;
    private String providerId;
    private Long pictureId;
}
