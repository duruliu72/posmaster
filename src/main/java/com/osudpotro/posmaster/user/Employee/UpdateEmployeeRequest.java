package com.osudpotro.posmaster.user.Employee;

import lombok.Data;

@Data
public class UpdateEmployeeRequest {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private Long multimediaId;
}
