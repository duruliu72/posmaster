package com.osudpotro.posmaster.user;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;
    private String email;
}