package com.osudpotro.posmaster.requisitiontype;

import org.springframework.stereotype.Component;

@Component
public class CustomRequisitionTypeMapper {
    //Mapping Here
    //Entity → DTO
    public RequisitionTypeDto toDto(RequisitionType requisitionType){
        RequisitionTypeDto requisitionTypeDto=new RequisitionTypeDto();
        requisitionTypeDto.setId(requisitionType.getId());
        requisitionTypeDto.setName(requisitionType.getName());
        requisitionTypeDto.setRequisitionTypeKey(requisitionType.getRequisitionTypeKey());
//        Set<RequisitionApproverDto> approvers = new HashSet<>();
//        for (RequisitionApprover approver:requisitionType.getRequisitionApprovers()){
//            RequisitionApproverDto approverDto=new RequisitionApproverDto();
//            approverDto.setId(approver.getId());
//            UserDto userDto=new UserDto();
//            userDto.setId(approver.getUser().getId());
//            userDto.setName(approver.getUser().getName());
//            userDto.setEmail(approver.getUser().getEmail());
//            approverDto.setUser(userDto);
//            if(approver.getNextUser()!=null){
//                UserDto nextUser=new UserDto();
//                nextUser.setId(approver.getNextUser().getId());
//                nextUser.setName(approver.getNextUser().getName());
//                nextUser.setEmail(approver.getNextUser().getEmail());
//                approverDto.setNextUser(nextUser);
//            }
//            approvers.add(approverDto);
//        }
//        requisitionTypeDto.setRequisitionApprovers(approvers);
        return requisitionTypeDto;
    }
}
