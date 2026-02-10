package com.osudpotro.posmaster.user.admin;

import lombok.Data;

@Data
public class UpdateAdminUserRequest {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private Long multimediaId;
}
