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
        if (requisitionApprover.getUser().getAdminUser() != null) {
            userDto.setUserName(requisitionApprover.getUser().getUserName());
            userDto.setEmail(requisitionApprover.getUser().getEmail());
        }
        requisitionApproverDto.setUser(userDto);
        if(requisitionApprover.getPrevUser()!=null){
            UserDto prevUserDto=new UserDto();
            prevUserDto.setId(requisitionApprover.getPrevUser().getId());
            if(requisitionApprover.getPrevUser().getAdminUser()!=null){
                prevUserDto.setUserName(requisitionApprover.getPrevUser().getUserName());
                prevUserDto.setEmail(requisitionApprover.getPrevUser().getEmail());
            }
            requisitionApproverDto.setPrevUser(prevUserDto);
        }
        if(requisitionApprover.getNextUser()!=null){
            UserDto nextUserDto=new UserDto();
            nextUserDto.setId(requisitionApprover.getNextUser().getId());
            if(requisitionApprover.getNextUser().getAdminUser()!=null){
                nextUserDto.setUserName(requisitionApprover.getNextUser().getUserName());
                nextUserDto.setEmail(requisitionApprover.getNextUser().getEmail());
            }
            requisitionApproverDto.setNextUser(nextUserDto);
        }
        return requisitionApproverDto;
    }
}
