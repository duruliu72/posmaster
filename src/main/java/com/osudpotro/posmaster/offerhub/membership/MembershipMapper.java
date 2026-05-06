package com.osudpotro.posmaster.offerhub.membership;

import org.springframework.stereotype.Component;

@Component
public class MembershipMapper {
    //Mapping Here
    //Entity → DTO
    public MembershipDto toDto(Membership membership) {
        MembershipDto membershipDto = new MembershipDto();
        membershipDto.setId(membership.getId());
        membershipDto.setName(membership.getName());
        membershipDto.setDiscount(membership.getDiscount());
        membershipDto.setMaxDiscount(membership.getMaxDiscount());
        membershipDto.setIsPercentage(membership.getIsPercentage());
        return membershipDto;
    }
}
