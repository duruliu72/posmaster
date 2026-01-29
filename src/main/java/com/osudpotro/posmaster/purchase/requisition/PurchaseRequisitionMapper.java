package com.osudpotro.posmaster.purchase.requisition;

import com.osudpotro.posmaster.organization.OrganizationDto;
import com.osudpotro.posmaster.purchase.dto.BranchDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PurchaseRequisitionMapper {
    //Mapping Here
    //Entity → DTO
    public PurchaseRequisitionDto toDto(PurchaseRequisition pr) {
        PurchaseRequisitionDto prDto = new PurchaseRequisitionDto();
        prDto.setId(pr.getId());
        prDto.setRequsitionRef(pr.getRequsitionRef());
        String purchaseCode = pr.getPurchaseType().getDescription();
        String purchaseKey = pr.getPurchaseType().getCode();
        prDto.setPurchaseType(purchaseCode);
        prDto.setPurchaseKey(purchaseKey);
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
        prDto.setTotalPrice(pr.getTotalPrice());
        prDto.setTotalQty(pr.getTotalQty());
        prDto.setTotalActualQty(pr.getTotalActualQty());
        prDto.setTotalActualPrice(pr.getTotalActualPrice());
        prDto.setTotalGiftQty(pr.getTotalGiftQty());
        prDto.setTotalGiftPrice(pr.getTotalGiftPrice());
        return prDto;
    }

    public PurchaseRequisitionWithItemPageResponse toMinDto(PurchaseRequisition pr, Page<PurchaseRequisitionItemDto> page) {
        PurchaseRequisitionWithItemPageResponse pageResponse = new PurchaseRequisitionWithItemPageResponse();
        pageResponse.setId(pr.getId());
        pageResponse.setRequsitionRef(pr.getRequsitionRef());
        String purchaseCode = pr.getPurchaseType().getDescription();
        String purchaseKey = pr.getPurchaseType().getCode();
        pageResponse.setPurchaseType(purchaseCode);
        pageResponse.setPurchaseKey(purchaseKey);
        OrganizationDto orgDto = new OrganizationDto();
        orgDto.setId(pr.getOrganization().getId());
        orgDto.setName(pr.getOrganization().getName());
        pageResponse.setOrganization(orgDto);
        BranchDto branchDto = new BranchDto();
        branchDto.setId(pr.getBranch().getId());
        branchDto.setName(pr.getBranch().getName());
        pageResponse.setBranch(branchDto);
        pageResponse.setCreatedAt(pr.getCreatedAt());
        if(pr.getRequisition()!=null){
            pageResponse.setRequisitionStatus(pr.getRequisition().getRequisitionStatus());
            pageResponse.setNote(pr.getRequisition().getNote());
        }

        pageResponse.setTotalPrice(pr.getTotalPrice());
        pageResponse.setTotalQty(pr.getTotalQty());

        pageResponse.setTotalActualQty(pr.getTotalActualQty());
        pageResponse.setTotalActualPrice(pr.getTotalActualPrice());
        pageResponse.setTotalGiftQty(pr.getTotalGiftQty());
        pageResponse.setTotalGiftPrice(pr.getTotalGiftPrice());
        //For Item Pagination
        pageResponse.setItems(page.getContent());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setPageNumber(page.getNumber());
        pageResponse.setPageSize(page.getSize());
        pageResponse.setTotalPages(page.getTotalPages());
        return pageResponse;
    }
}
