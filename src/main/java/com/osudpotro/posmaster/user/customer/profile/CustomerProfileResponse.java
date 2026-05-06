package com.osudpotro.posmaster.user.customer.profile;
import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.multimedia.MultimediaDto;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CustomerProfileResponse {
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
    private String provider;
    private String providerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private CustomerLoginDeviceResponse currentActiveSession;
    private List<CustomerLoginDeviceResponse> recentLoginHistory;
    private Long totalLoginCount;
}