package com.osudpotro.posmaster.user.auth;

import com.osudpotro.posmaster.user.admin.EmailOrMobileRequired;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@EmailOrMobileRequired
public class LoginRequest {
    private String email;
    private String mobile;
    @NotBlank(message = "Password is required")
    private String password;
    //    1=Admin,2=Employee
    private Integer userType;
}