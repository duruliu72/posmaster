package com.osudpotro.posmaster.web.customer;

import lombok.Data;

@Data
public class WebCustomerLoginRequest {
    private String email;
    private String mobile;
    private String otpCode;
    private String provider;
    private String providerId;
    private String password;
    private String gender;
    private String profilePic;
}
