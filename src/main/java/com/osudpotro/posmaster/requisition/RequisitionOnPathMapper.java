package com.osudpotro.posmaster.requisition;

import com.osudpotro.posmaster.organization.OrganizationDto;
import com.osudpotro.posmaster.purchase.dto.BranchDto;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionDto;
import com.osudpotro.posmaster.requisitiontype.RequisitionTypeDto;
import org.springframework.stereotype.Component;

@Component
public class RequisitionOnPathMapper {
    //Mapping Here
    //Entity → DTO
    public RequisitionOnPathDto toDto(RequisitionOnPath rop) {
        RequisitionOnPathDto ropDto = new RequisitionOnPathDto();
        ropDto.setId(rop.getId());
        ropDto.setReviewCount(rop.getReviewCount());
        ropDto.setApprovedStatus(rop.getApprovedStatus());
        ropDto.setComment(rop.getComment());
        // For Requisition
        if(rop.getRequisition()!=null){
            RequisitionDto requisitionDto = new RequisitionDto();
            requisitionDto.setId(rop.getRequisition().getId());
            requisitionDto.setRequsitionRef(rop.getRequisition().getRequsitionRef());
            requisitionDto.setRequisitionStatus(rop.getRequisition().getRequisitionStatus());
            requisitionDto.setNote(rop.getRequisition().getNote());
            requisitionDto.setReviewCount(rop.getRequisition().getReviewCount());
            requisitionDto.setCreatedAt(rop.getRequisition().getCreatedAt());
            if(rop.getRequisition().getRequisitionType()!=null){
                RequisitionTypeDto requisitionTypeDto=new RequisitionTypeDto();
                requisitionTypeDto.setId(rop.getRequisition().getRequisitionType().getId());
                requisitionTypeDto.setName(rop.getRequisition().getRequisitionType().getName());
                requisitionTypeDto.setRequisitionTypeKey(rop.getRequisition().getRequisitionType().getRequisitionTypeKey());
                requisitionDto.setRequisitionType(requisitionTypeDto);
                String requisitionTypeKey=rop.getRequisition().getRequisitionType().getRequisitionTypeKey();
                if(requisitionTypeKey.equalsIgnoreCase("PROCUREMENT_REQUISITION")||requisitionTypeKey.equalsIgnoreCase("COMPANY_PURCHASE_REQUISITION")||requisitionTypeKey.equalsIgnoreCase("PURCHASE_ORDER_REQUISITION")||requisitionTypeKey.equalsIgnoreCase("LOCAL_PURCHASE_REQUISITION")){
                    if(rop.getRequisition().getPurchaseRequisition()!=null){
                        var pr=rop.getRequisition().getPurchaseRequisition();
                        PurchaseRequisitionDto prDto = new PurchaseRequisitionDto();
                        String purchaseCode = pr.getPurchaseType().getDescription();
                        String purchaseKey = pr.getPurchaseType().getCode();
                        prDto.setPurchaseType(purchaseCode);
                        prDto.setPurchaseKey(purchaseKey);
                        prDto.setId(pr.getId());
                        prDto.setRequsitionRef(pr.getRequsitionRef());
                        OrganizationDto orgDto = new OrganizationDto();
                        orgDto.setId(pr.getOrganization().getId());
                        orgDto.setName(pr.getOrganization().getName());
                        prDto.setOrganization(orgDto);
                        BranchDto branchDto = new BranchDto();
                        branchDto.setId(pr.getBranch().getId());
                        branchDto.setName(pr.getBranch().getName());
                        prDto.setBranch(branchDto);
                        prDto.setCreatedAt(pr.getCreatedAt());
                        if(pr.getRequisition()!=null){
                            prDto.setRequisitionStatus(pr.getRequisition().getRequisitionStatus());
                            prDto.setNote(pr.getRequisition().getNote());
                        }
                        requisitionDto.setPurchaseRequisition(prDto);
                    }
                }
            }
            ropDto.setRequisition(requisitionDto);
        }
        UserDto userDto=new UserDto();
        userDto.setId(rop.getUser().getId());
        userDto.setUserName(rop.getUser().getAdminUser().getUserName());
        userDto.setEmail(rop.getUser().getAdminUser().getEmail());
        ropDto.setUser(userDto);

        if(rop.getPrevUser()!=null){
            UserDto prevUserDto=new UserDto();
            prevUserDto.setId(rop.getPrevUser().getId());
            prevUserDto.setUserName(rop.getPrevUser().getAdminUser().getUserName());
            prevUserDto.setEmail(rop.getPrevUser().getAdminUser().getEmail());
            ropDto.setPrevUser(prevUserDto);
        }
        if(rop.getNextUser()!=null){
            UserDto nextUserDto=new UserDto();
            nextUserDto.setId(rop.getNextUser().getId());
            nextUserDto.setUserName(rop.getNextUser().getAdminUser().getUserName());
            nextUserDto.setEmail(rop.getNextUser().getAdminUser().getEmail());
            ropDto.setNextUser(nextUserDto);
        }
        return ropDto;
    }
}
