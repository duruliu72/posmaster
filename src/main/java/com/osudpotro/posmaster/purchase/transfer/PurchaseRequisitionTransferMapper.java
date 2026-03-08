package com.osudpotro.posmaster.purchase.transfer;

import com.osudpotro.posmaster.organization.OrganizationDto;
import com.osudpotro.posmaster.purchase.dto.BranchDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PurchaseRequisitionTransferMapper {
    //Mapping Here
    //Entity → DTO
    public PurchaseRequisitionTransferDto toDto(PurchaseRequisitionTransfer prt) {
        PurchaseRequisitionTransferDto prtDto = new PurchaseRequisitionTransferDto();
        prtDto.setId(prt.getId());
        prtDto.setRequsitionRef(prt.getRequsitionRef());
        String purchaseCode = prt.getPurchaseType().getDescription();
        String purchaseKey = prt.getPurchaseType().getCode();
        prtDto.setPurchaseType(purchaseCode);
        prtDto.setPurchaseKey(purchaseKey);
        OrganizationDto orgDto = new OrganizationDto();
        orgDto.setId(prt.getOrganization().getId());
        orgDto.setName(prt.getOrganization().getName());
        prtDto.setOrganization(orgDto);
        BranchDto branchDto = new BranchDto();
        branchDto.setId(prt.getBranch().getId());
        branchDto.setName(prt.getBranch().getName());
        prtDto.setBranch(branchDto);
        prtDto.setOverallDiscount(prtDto.getOverallDiscount());
        prtDto.setTotalPrice(prt.getTotalPrice());
        prtDto.setTotalQty(prt.getTotalQty());
        prtDto.setTotalGiftOrBonusQty(prt.getTotalGiftOrBonusQty());
        prtDto.setTotalGiftOrBonusPrice(prt.getTotalGiftOrBonusPrice());
        prtDto.setPurchaseInvoices(prt.getPurchaseInvoices());
        prtDto.setPurchaseInvoiceDocs(prt.getPurchaseInvoiceDocs());
        prtDto.setOrderRefs(prt.getOrderRefs());
        return prtDto;
    }

    public PurchaseRequisitionTransferWithItemPageResponse toMinDto(PurchaseRequisitionTransfer pr, Page<PurchaseRequisitionItemTransferDto> page) {
        PurchaseRequisitionTransferWithItemPageResponse pageResponse = new PurchaseRequisitionTransferWithItemPageResponse();
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
        pageResponse.setOverallDiscount(pr.getOverallDiscount());
        pageResponse.setTotalPrice(pr.getTotalPrice());
        pageResponse.setTotalQty(pr.getTotalQty());
        pageResponse.setTotalGiftOrBonusQty(pr.getTotalGiftOrBonusQty());
        pageResponse.setTotalGiftOrBonusPrice(pr.getTotalGiftOrBonusPrice());
        pageResponse.setPurchaseInvoices(pr.getPurchaseInvoices());
        pageResponse.setPurchaseInvoiceDocs(pr.getPurchaseInvoiceDocs());
        pageResponse.setOrderRefs(pr.getOrderRefs());
        //For Item Pagination
        pageResponse.setItems(page.getContent());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setPageNumber(page.getNumber());
        pageResponse.setPageSize(page.getSize());
        pageResponse.setTotalPages(page.getTotalPages());
        return pageResponse;
    }
}
