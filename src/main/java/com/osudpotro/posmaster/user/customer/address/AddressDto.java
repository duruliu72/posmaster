package com.osudpotro.posmaster.user.customer.address;

import com.osudpotro.posmaster.user.UserPlainDto;
import com.osudpotro.posmaster.user.customer.CustomerDto;
import lombok.Data;

@Data
public class AddressDto {
    private Long id;
    private UserPlainDto user;
    private CustomerDto customer;
    private String name;
    private String email;
    private String mobile;
    private Integer addressType;
    private Integer addressCategory;
    private Double latitude;
    private Double longitude;
    private Double accuracy;
    private String locationName;
    private String locationDesc;
    private Boolean isDefault;
}
