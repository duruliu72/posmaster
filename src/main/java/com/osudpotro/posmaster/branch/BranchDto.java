package com.osudpotro.posmaster.branch;
import com.osudpotro.posmaster.multimedia.MultimediaDto;
import com.osudpotro.posmaster.organization.OrganizationDto;
import lombok.Data;

@Data
public class BranchDto {
    private Long id;
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
    private MultimediaDto media;
    private OrganizationDto organization;
}