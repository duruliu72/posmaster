package com.osudpotro.posmaster.tms.vehicledriver;

import com.osudpotro.posmaster.multimedia.MultimediaDto;
import lombok.Data;

@Data
public class VehicleDriverDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String secondaryEmail;
    private String secondaryMobile;
    private Integer gender;
    private String otpCode;
    private String provider;
    private String providerId;
    private MultimediaDto profilePic;
}
