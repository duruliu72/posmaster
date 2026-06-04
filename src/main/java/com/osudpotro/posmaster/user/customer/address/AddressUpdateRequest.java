package com.osudpotro.posmaster.user.customer.address;

import lombok.Data;

@Data
public class AddressUpdateRequest {
    private String fullName;
    private String email;
    private String mobile;
    private Integer addressType;
    private Integer addressCategory;
    private String placeId;
    private Double latitude;
    private Double longitude;
    private Double accuracy;
    private String location;
    private String locationDesc;
    private String locationName;
    private Boolean isDefault;
    private Long areaId;
}
