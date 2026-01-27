package com.osudpotro.posmaster.requisition;

import com.osudpotro.posmaster.requisitiontype.RequisitionTypeDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RequisitionMapper {
    //Mapping Here
    //Entity → DTO
    public RequisitionDto toDto(Requisition requisition) {
        RequisitionDto requisitionDto = new RequisitionDto();
        requisitionDto.setId(requisition.getId());
        requisitionDto.setRequsitionRef(requisition.getRequsitionRef());
        requisitionDto.setRequisitionStatus(requisition.getRequisitionStatus());
        requisitionDto.setNote(requisition.getNote());
        requisitionDto.setReviewCount(requisition.getReviewCount());
        if(requisition.getRequisitionType()!=null){
            RequisitionTypeDto requisitionTypeDto=new RequisitionTypeDto();
            requisitionTypeDto.setId(requisition.getRequisitionType().getId());
            requisitionTypeDto.setName(requisition.getRequisitionType().getName());
            requisitionTypeDto.setRequisitionTypeKey(requisition.getRequisitionType().getRequisitionTypeKey());
            requisitionDto.setRequisitionType(requisitionTypeDto);
        }
        List<RequisitionOnPathDto> requisitionOnPaths= new ArrayList<>();
        if(requisition.getRequisitionOnPaths()!=null&& !requisition.getRequisitionOnPaths().isEmpty()){
            for (var rop:requisition.getRequisitionOnPaths()){
                RequisitionOnPathDto ropDto=new RequisitionOnPathDto();
                ropDto.setId(rop.getId());
                ropDto.setReviewCount(rop.getReviewCount());
                ropDto.setApprovedStatus(rop.getApprovedStatus());
                ropDto.setComment(rop.getComment());
                requisitionOnPaths.add(ropDto);
            }
        }
        requisitionDto.setRequisitionOnPaths(requisitionOnPaths);
        return requisitionDto;
    }
}
