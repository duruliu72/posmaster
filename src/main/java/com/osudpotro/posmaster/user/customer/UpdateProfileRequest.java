package com.osudpotro.posmaster.user.customer;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String userName;
    private String firstName;
    private String lastName;
    private String secondaryEmail;
    private String secondaryMobile;
    private Integer gender;
}