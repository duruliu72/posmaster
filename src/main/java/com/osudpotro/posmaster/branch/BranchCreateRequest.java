package com.osudpotro.posmaster.branch;

import lombok.Data;

@Data
public class BranchCreateRequest {
    private String name;
    private String location;
    private String address;
    private String district;
    private String country;
    private double latitude;
    private double longitude;
    private double accuracy;
    private String mobile;
    private String licenceNo;
    private Long multimediaId;
    private Long organizationId;
}