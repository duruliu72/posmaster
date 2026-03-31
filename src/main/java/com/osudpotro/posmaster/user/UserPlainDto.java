package com.osudpotro.posmaster.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPlainDto {
    private Long id;
    private String userName;
    private String email;
    private String mobile;
}