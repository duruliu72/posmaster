package com.osudpotro.posmaster.user;

import lombok.Data;

@Data
public class UserFilter {
    private String name;
    private String email;
    private String mobile;
    private String secondaryEmail;
    private String secondaryMobile;
    private Integer status;
}
