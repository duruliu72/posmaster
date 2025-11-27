package com.osudpotro.posmaster.user;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RegiterUserRequest {
    @NotBlank(message = "Name is Required")
    @Size(max = 255,message = "Name must be less than 255 charters")
    private String name;

    @NotBlank(message = "Email is Required")
    @Email (message = "Email must be valid")
    @Lowercase(message = "Email must be in lowercase")
    private String email;

    @NotBlank(message = "Password is Required")
    @Size(min = 6,max = 25,message = "Password must be between 6 to 25 charters")
    private String password;
}