package com.osudpotro.posmaster.web.customer;

import lombok.Data;

@Data
public class WebCustomerOtpRequest {
    private String email;
    private String mobile;
}
