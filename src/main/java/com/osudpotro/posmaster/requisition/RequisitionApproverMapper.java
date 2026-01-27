package com.osudpotro.posmaster.requisition;

import org.springframework.stereotype.Component;

@Component
public class RequisitionApproverMapper {
    //Mapping Here
    //Entity → DTO
    public RequisitionApproverDto toDto(RequisitionApprover requisitionApprover) {
        RequisitionApproverDto requisitionApproverDto = new RequisitionApproverDto();
        requisitionApproverDto.setId(requisitionApprover.getId());
        UserDto userDto=new UserDto();
        userDto.setId(requisitionApprover.getUser().getId());
        userDto.setName(requisitionApprover.getUser().getName());
        userDto.setEmail(requisitionApprover.getUser().getEmail());
        requisitionApproverDto.setUser(userDto);
        if(requisitionApprover.getPrevUser()!=null){
            UserDto prevUserDto=new UserDto();
            prevUserDto.setId(requisitionApprover.getPrevUser().getId());
            prevUserDto.setName(requisitionApprover.getPrevUser().getName());
            prevUserDto.setEmail(requisitionApprover.getPrevUser().getEmail());
            requisitionApproverDto.setPrevUser(prevUserDto);
        }
        if(requisitionApprover.getNextUser()!=null){
            UserDto nextUserDto=new UserDto();
            nextUserDto.setId(requisitionApprover.getNextUser().getId());
            nextUserDto.setName(requisitionApprover.getNextUser().getName());
            nextUserDto.setEmail(requisitionApprover.getNextUser().getEmail());
            requisitionApproverDto.setNextUser(nextUserDto);
        }
        return requisitionApproverDto;
    }
}
