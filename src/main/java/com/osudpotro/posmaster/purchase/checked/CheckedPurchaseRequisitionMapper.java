package com.osudpotro.posmaster.purchase.checked;

import com.osudpotro.posmaster.branch.BranchDto;
import com.osudpotro.posmaster.branch.BranchMapper;
import com.osudpotro.posmaster.organization.OrganizationDto;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisition;
import com.osudpotro.posmaster.user.CustomUserMapper;
import com.osudpotro.posmaster.user.UserPlainDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
@Component
public class CheckedPurchaseRequisitionMapper {
    @Autowired
    private BranchMapper branchMapper;
    @Autowired
    private CustomUserMapper customUserMapper;
    //Mapping Here
    //Entity → DTO
    public CheckedPurchaseRequisitionDto toDto(CheckedPurchaseRequisition cpr) {
        if (cpr == null) {
            return null;
        }
        CheckedPurchaseRequisitionDto cprDto = new CheckedPurchaseRequisitionDto();
        cprDto.setId(cpr.getId());
        cprDto.setRequsitionRef(cpr.getRequsitionRef());
        cprDto.setOverallDiscount(cpr.getOverallDiscount());
        cprDto.setTotalPrice(cpr.getTotalPrice());
        cprDto.setTotalQty(cpr.getTotalQty());
        cprDto.setTotalGiftOrBonusQty(cpr.getTotalGiftOrBonusQty());
        cprDto.setTotalGiftOrBonusPrice(cpr.getTotalGiftOrBonusPrice());
        cprDto.setPurchaseInvoices(cpr.getPurchaseInvoices());
        cprDto.setPurchaseInvoiceDocs(cpr.getPurchaseInvoiceDocs());
        cprDto.setOrderRefs(cpr.getOrderRefs());
        if (cpr.getCheckByBranchMan() != null) {
            var checkByBranchMan = cpr.getCheckByBranchMan();
            UserPlainDto checkByBranchManDto = new UserPlainDto();
            checkByBranchManDto.setUserName(checkByBranchMan.getUserName());
            checkByBranchManDto.setId(checkByBranchMan.getId());
            checkByBranchManDto.setMobile(checkByBranchMan.getMobile());
            checkByBranchManDto.setEmail(checkByBranchMan.getEmail());
            cprDto.setCheckByBranchMan(checkByBranchManDto);
        }
        cprDto.setCheckByBranchAt(cpr.getCheckByBranchAt());
        if (cpr.getCheckByAdmin() != null) {
            var checkByAdmin = cpr.getCheckByAdmin();
            UserPlainDto checkByAdminManDto = new UserPlainDto();
            checkByAdminManDto.setUserName(checkByAdmin.getUserName());
            checkByAdminManDto.setId(checkByAdmin.getId());
            checkByAdminManDto.setMobile(checkByAdmin.getMobile());
            checkByAdminManDto.setEmail(checkByAdmin.getEmail());
            cprDto.setCheckByAdmin(checkByAdminManDto);
        }
        cprDto.setCheckByAdminAt(cpr.getCheckByAdminAt());
        if (cpr.getAddByInventoryMan() != null) {
            var addByInventoryMan = cpr.getAddByInventoryMan();
            UserPlainDto addByInventoryManDto = new UserPlainDto();
            addByInventoryManDto.setUserName(addByInventoryMan.getUserName());
            addByInventoryManDto.setId(addByInventoryMan.getId());
            addByInventoryManDto.setMobile(addByInventoryMan.getMobile());
            addByInventoryManDto.setEmail(addByInventoryMan.getEmail());
            cprDto.setAddByInventoryMan(addByInventoryManDto);
        }
        cprDto.setAddByInventoryManAt(cpr.getAddByInventoryManAt());
        cprDto.setCheckedStatus(cpr.getCheckedStatus());
        if (cpr.getPurchaseRequisition() != null) {
            PurchaseRequisition pr = cpr.getPurchaseRequisition();
            cprDto.setPurchaseRequisitionId(pr.getId());
            String purchaseCode = pr.getPurchaseType().getDescription();
            String purchaseKey = pr.getPurchaseType().getCode();
            cprDto.setPurchaseType(purchaseCode);
            cprDto.setPurchaseKey(purchaseKey);
            //        Organization
            OrganizationDto orgDto = new OrganizationDto();
            orgDto.setId(pr.getOrganization().getId());
            orgDto.setName(pr.getOrganization().getName());
            cprDto.setOrganization(orgDto);
            //  Branch
            if (pr.getBranch() != null) {
                BranchDto branchDto = new BranchDto();
                branchDto.setId(pr.getBranch().getId());
                branchDto.setName(pr.getBranch().getName());
                cprDto.setBranch(branchMapper.toDto(pr.getBranch()));
            }
            if(pr.getRequisition()!=null){
                cprDto.setRequisitionId(pr.getRequisition().getId());
                cprDto.setRequisitionStatus(pr.getRequisition().getRequisitionStatus());
            }
        }
        cprDto.setCreatedAt(cpr.getCreatedAt());
        cprDto.setCheckedStatus(cpr.getCheckedStatus());
        return cprDto;
    }
    public CheckedPurchaseRequisitionWithItemPageResponse toMinDto(CheckedPurchaseRequisition cpr, Page<CheckedPurchaseRequisitionItemDto> page) {
        if (cpr == null) {
            return null;
        }
        CheckedPurchaseRequisitionWithItemPageResponse pageResponse = new CheckedPurchaseRequisitionWithItemPageResponse();
        pageResponse.setId(cpr.getId());
        pageResponse.setRequsitionRef(cpr.getRequsitionRef());
        pageResponse.setOverallDiscount(cpr.getOverallDiscount());
        pageResponse.setTotalPrice(cpr.getTotalPrice());
        pageResponse.setTotalQty(cpr.getTotalQty());
        pageResponse.setTotalGiftOrBonusQty(cpr.getTotalGiftOrBonusQty());
        pageResponse.setTotalGiftOrBonusPrice(cpr.getTotalGiftOrBonusPrice());
        pageResponse.setPurchaseInvoices(cpr.getPurchaseInvoices());
        pageResponse.setPurchaseInvoiceDocs(cpr.getPurchaseInvoiceDocs());
        pageResponse.setOrderRefs(cpr.getOrderRefs());
        if (cpr.getCheckByBranchMan() != null) {
            var checkByBranchMan = cpr.getCheckByBranchMan();
            UserPlainDto checkByBranchManDto = new UserPlainDto();
            checkByBranchManDto.setUserName(checkByBranchMan.getUserName());
            checkByBranchManDto.setId(checkByBranchMan.getId());
            checkByBranchManDto.setMobile(checkByBranchMan.getMobile());
            checkByBranchManDto.setEmail(checkByBranchMan.getEmail());
            pageResponse.setCheckByBranchMan(checkByBranchManDto);
        }
        pageResponse.setCheckByBranchAt(cpr.getCheckByBranchAt());
        if (cpr.getCheckByAdmin() != null) {
            var checkByAdmin = cpr.getCheckByAdmin();
            UserPlainDto checkByAdminManDto = new UserPlainDto();
            checkByAdminManDto.setUserName(checkByAdmin.getUserName());
            checkByAdminManDto.setId(checkByAdmin.getId());
            checkByAdminManDto.setMobile(checkByAdmin.getMobile());
            checkByAdminManDto.setEmail(checkByAdmin.getEmail());
            pageResponse.setCheckByAdmin(checkByAdminManDto);
        }
        pageResponse.setCheckByAdminAt(cpr.getCheckByAdminAt());
        if (cpr.getAddByInventoryMan() != null) {
            var addByInventoryMan = cpr.getAddByInventoryMan();
            UserPlainDto addByInventoryManDto = new UserPlainDto();
            addByInventoryManDto.setUserName(addByInventoryMan.getUserName());
            addByInventoryManDto.setId(addByInventoryMan.getId());
            addByInventoryManDto.setMobile(addByInventoryMan.getMobile());
            addByInventoryManDto.setEmail(addByInventoryMan.getEmail());
            pageResponse.setAddByInventoryMan(addByInventoryManDto);
        }
        pageResponse.setAddByInventoryManAt(cpr.getAddByInventoryManAt());
        pageResponse.setCheckedStatus(cpr.getCheckedStatus());
        if (cpr.getPurchaseRequisition() != null) {
            PurchaseRequisition pr = cpr.getPurchaseRequisition();
            pageResponse.setPurchaseRequisitionId(pr.getId());
            String purchaseCode = pr.getPurchaseType().getDescription();
            String purchaseKey = pr.getPurchaseType().getCode();
            pageResponse.setPurchaseType(purchaseCode);
            pageResponse.setPurchaseKey(purchaseKey);
            //        Organization
            OrganizationDto orgDto = new OrganizationDto();
            orgDto.setId(pr.getOrganization().getId());
            orgDto.setName(pr.getOrganization().getName());
            pageResponse.setOrganization(orgDto);
            //  Branch
            if (pr.getBranch() != null) {
                BranchDto branchDto = new BranchDto();
                branchDto.setId(pr.getBranch().getId());
                branchDto.setName(pr.getBranch().getName());
                pageResponse.setBranch(branchMapper.toDto(pr.getBranch()));
            }
            if(pr.getRequisition()!=null){
                pageResponse.setRequisitionId(pr.getRequisition().getId());
                pageResponse.setRequisitionStatus(pr.getRequisition().getRequisitionStatus());
            }
        }
        pageResponse.setCreatedAt(cpr.getCreatedAt());
        pageResponse.setCheckedStatus(cpr.getCheckedStatus());
        //For Item Pagination
        pageResponse.setItems(page.getContent());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setPageNumber(page.getNumber());
        pageResponse.setPageSize(page.getSize());
        pageResponse.setTotalPages(page.getTotalPages());
        return pageResponse;
    }
}
