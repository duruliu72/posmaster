package com.osudpotro.posmaster.user.Employee;

import com.osudpotro.posmaster.user.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterEmployeeRequest {
    private String firstName;
    private String lastName;
    private String userName;
    @NotBlank(message = "Email is Required")
    @Email(message = "Email must be valid")
    @Lowercase(message = "Email must be in lowercase")
    private String email;
    private String mobile;
    @NotBlank(message = "Password is Required")
    @Size(min = 6,max = 25,message = "Password must be between 6 to 25 charters")
    private String password;
}
