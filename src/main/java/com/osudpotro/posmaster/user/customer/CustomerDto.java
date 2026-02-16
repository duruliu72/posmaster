package com.osudpotro.posmaster.user.customer;

import com.osudpotro.posmaster.multimedia.Multimedia;
import lombok.Data;

@Data
public class CustomerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String password;
    private String secondaryEmail;
    private String secondaryMobile;
    private Integer gender;
    private String otpCode;
    private String provider;
    private String providerId;
    private Multimedia profilePic;
}
