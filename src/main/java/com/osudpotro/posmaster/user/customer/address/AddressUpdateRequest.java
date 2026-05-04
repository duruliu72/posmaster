package com.osudpotro.posmaster.user.customer.address;

import lombok.Data;

@Data
public class AddressUpdateRequest {
    private String name;
    private String email;
    private String mobile;
    private Integer addressType;
    private Integer addressCategory;
    private Double latitude;
    private Double longitude;
    private Double accuracy;
    private String location;
    private String locationDesc;
    private Boolean isDefault;
    private Long areaId;
}
