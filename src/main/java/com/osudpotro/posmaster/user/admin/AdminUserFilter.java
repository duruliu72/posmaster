package com.osudpotro.posmaster.user.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminUserFilter {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private Integer status;
    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;
}
