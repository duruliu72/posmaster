package com.osudpotro.posmaster.user.customer;

import lombok.Data;

@Data
public class UpdateForUserRequest {
    private String userName;
    private String email;
    private String mobile;
}
