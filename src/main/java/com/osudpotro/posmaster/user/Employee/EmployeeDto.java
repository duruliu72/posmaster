package com.osudpotro.posmaster.user.Employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmployeeDto {
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private Long userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
