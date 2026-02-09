package com.osudpotro.posmaster.user.customer;

import lombok.Data;
@Data
public class CustomerUpdateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String secondaryEmail;
    private String secondaryPhone;
    private Integer gender;
    private String otpCode;
    private String provider;
    private String providerId;
    private Long pictureId;
}
