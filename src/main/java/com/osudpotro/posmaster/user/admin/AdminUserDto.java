package com.osudpotro.posmaster.user.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.osudpotro.posmaster.multimedia.MultimediaDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminUserDto {
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String secondaryEmail;
    private String secondaryMobile;
    private Integer gender;
    private MultimediaDto profilePic;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
