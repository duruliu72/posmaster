package com.osudpotro.posmaster.user.customer;

import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.offerhub.membership.MembershipDto;
import com.osudpotro.posmaster.user.customer.address.AddressDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerDto {
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String password;
    private String secondaryEmail;
    private String secondaryMobile;
    private Integer gender;
    private String otpCode;
    private String provider;
    private String providerId;
    private Multimedia profilePic;
    private MembershipDto membership;
    private BigDecimal netWalletAmount;
    private List<AddressDto> addresses = new ArrayList<>();
}
