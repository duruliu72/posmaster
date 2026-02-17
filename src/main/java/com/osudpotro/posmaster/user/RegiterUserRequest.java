package com.osudpotro.posmaster.user;
import lombok.Data;
@Data
public class RegiterUserRequest {
    private String userName;
    private String email;
    private String password;
}