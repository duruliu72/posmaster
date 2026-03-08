package com.osudpotro.posmaster.branch;

import lombok.Data;

@Data
public class BranchUpdateRequest {
    private String name;
    private String locationName;
    private String address;
    private String district;
    private String country;
    private String placeId;
    private double latitude;
    private double longitude;
    private double accuracy;
    private String mobile;
    private String licenceNo;
    private Long multimediaId;
    private Long organizationId;
}