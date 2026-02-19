package com.osudpotro.posmaster.tms.driver;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DriverLoginRequest {
    //    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    //    @NotBlank(message = "Phone is required")
    private String mobile;
    @NotBlank(message = "Password is required")
    private String password;
}
