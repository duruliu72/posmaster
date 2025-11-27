package com.osudpotro.posmaster.requisitiontype;

import com.osudpotro.posmaster.requisition.Approver;
import com.osudpotro.posmaster.requisition.ApproverDto;
import com.osudpotro.posmaster.requisition.UserDto;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CustomRequisitionMapper {
    //Mapping Here
    //Entity → DTO
    public RequisitionTypeDto toDto(RequisitionType requisitionType){
        RequisitionTypeDto requisitionTypeDto=new RequisitionTypeDto();
        requisitionTypeDto.setId(requisitionType.getId());
        requisitionTypeDto.setName(requisitionType.getName());
        requisitionTypeDto.setRequisitionTypeKey(requisitionType.getRequisitionTypeKey());
        Set<ApproverDto> approvers = new HashSet<>();
        for (Approver approver:requisitionType.getApprovers()){
            ApproverDto approverDto=new ApproverDto();
            UserDto userDto=new UserDto();
            userDto.setId(approver.getUser().getId());
            userDto.setName(approver.getUser().getName());
            userDto.setEmail(approver.getUser().getEmail());
            approverDto.setUser(userDto);
            if(approver.getNextUser()!=null){
                UserDto nextUser=new UserDto();
                nextUser.setId(approver.getNextUser().getId());
                nextUser.setName(approver.getNextUser().getName());
                nextUser.setEmail(approver.getNextUser().getEmail());
                approverDto.setNextUser(nextUser);
            }
            approvers.add(approverDto);
        }
        requisitionTypeDto.setApprovers(approvers);
        return requisitionTypeDto;
    }
}
