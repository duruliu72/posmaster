package com.osudpotro.posmaster.user.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminUserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
