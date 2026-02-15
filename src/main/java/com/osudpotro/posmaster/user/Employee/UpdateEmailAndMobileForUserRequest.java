package com.osudpotro.posmaster.user.Employee;

import lombok.Data;

@Data
public class UpdateEmailAndMobileForUserRequest {
    private String userName;
    private String email;
    private String mobile;
}
