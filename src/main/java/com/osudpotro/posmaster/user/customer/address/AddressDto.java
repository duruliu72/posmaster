package com.osudpotro.posmaster.user.customer.address;

import lombok.Data;

@Data
public class AddressDto {
    private Long id;
    private Long userId;
    private Long customerId;
    private String customerName;
    private String fullName;
    private String email;
    private String mobile;
    private Integer addressType;
    private Integer addressCategory;
    private Long areaId;
    private String areaName;
    private String placeId;
    private Double latitude;
    private Double longitude;
    private Double accuracy;
    private String locationName;
    private String locationDesc;
    private Boolean isDefault;
}
