package com.osudpotro.posmaster.branch;

import com.osudpotro.posmaster.multimedia.MultimediaDto;
import com.osudpotro.posmaster.organization.Organization;
import com.osudpotro.posmaster.organization.OrganizationDto;
import org.springframework.stereotype.Component;

@Component
public class CustomBranchMapper {
    //Mapping Here
    //Entity → DTO
    public BranchDto toDto(Branch branch) {
        BranchDto branchDto = new BranchDto();
        branchDto.setId(branch.getId());
        branchDto.setName(branch.getName());
        branchDto.setLocation(branch.getLocation());
        branchDto.setAddress(branch.getAddress());
        branchDto.setDistrict(branch.getDistrict());
        branchDto.setCountry(branch.getCountry());
        branchDto.setLatitude(branch.getLatitude());
        branchDto.setLongitude(branch.getLongitude());
        branchDto.setAccuracy(branch.getAccuracy());
        branchDto.setMobile(branch.getMobile());
        branchDto.setLicenceNo(branch.getLicenceNo());
        if (branch.getMedia() != null && branch.getMedia().getImageUrl() != null) {
            MultimediaDto multimediaDto = new MultimediaDto();
            multimediaDto.setId(branch.getMedia().getId());
            multimediaDto.setName(branch.getMedia().getName());
            multimediaDto.setImageUrl(branch.getMedia().getImageUrl());
            multimediaDto.setMediaType(branch.getMedia().getMediaType());
            multimediaDto.setSourceLink(branch.getMedia().getSourceLink());
            branchDto.setMedia(multimediaDto);
        }
        if (branch.getOrganization() != null && branch.getOrganization().getName() != null) {
            OrganizationDto organizationDto = new OrganizationDto();
            organizationDto.setId(branch.getOrganization().getId());
            organizationDto.setName(branch.getOrganization().getName());
            branchDto.setOrganization(organizationDto);
        }
        return branchDto;
    }
}


