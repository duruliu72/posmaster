package com.osudpotro.posmaster.dispatch;


import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.branch.BranchDto;
import com.osudpotro.posmaster.organization.Organization;
import com.osudpotro.posmaster.organization.OrganizationDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class DispatchMapper {
    //Mapping Here
    //Entity → DTO
    public DispatchDto toDto(Dispatch dispatch) {
        DispatchDto dispatchDto=new DispatchDto();
        dispatchDto.setId(dispatch.getId());
        dispatchDto.setDispatchRef(dispatch.getDispatchRef());
        dispatchDto.setDispatchInvoice(dispatch.getDispatchInvoice());
        dispatchDto.setDispatchStatus(dispatch.getDispatchStatus());
        if (dispatch.getOrganization() != null) {
            Organization org=dispatch.getOrganization();
            OrganizationDto orgDto=new OrganizationDto();
            orgDto.setId(org.getId());
            orgDto.setName(org.getName());
            dispatchDto.setOrganization(orgDto);
        }
        if (dispatch.getRequesterBranch() != null) {
            Branch requestedBranch=dispatch.getRequesterBranch();
            BranchDto requestedBranchDto=new BranchDto();
            requestedBranchDto.setId(requestedBranch.getId());
            requestedBranchDto.setName(requestedBranch.getName());
            dispatchDto.setRequesterBranch(requestedBranchDto);
        }
        if (dispatch.getAcceptorBranch() != null) {
            Branch requestReceivedBranch=dispatch.getAcceptorBranch();
            BranchDto requestReceivedBranchDto=new BranchDto();
            requestReceivedBranchDto.setId(requestReceivedBranch.getId());
            requestReceivedBranchDto.setName(requestReceivedBranch.getName());
            dispatchDto.setAcceptorBranch(requestReceivedBranchDto);
        }
//        if (dispatch.getLoadedBy() != null) {
//            var loadedBy = goodsOnTrip.getLoadedBy();
//            UserPlainDto loadedByDto = new UserPlainDto();
//            loadedByDto.setUserName(loadedBy.getUserName());
//            loadedByDto.setId(loadedBy.getId());
//            loadedByDto.setMobile(loadedBy.getMobile());
//            loadedByDto.setEmail(loadedBy.getEmail());
//            goodsOnTripDto.setLoadedBy(loadedByDto);
//        }
        dispatchDto.setTotalDispatchQty(dispatch.getTotalQty());
        dispatchDto.setCreatedAt(dispatch.getCreatedAt());
        dispatchDto.setTotalPurchasePrice(dispatch.getTotalPurchasePrice());
        return dispatchDto;
    }
    public DispatchWithItemPageResponse toMinDto(Dispatch dispatch, Page<DispatchItemDto> page) {
        DispatchWithItemPageResponse pageResponse=new DispatchWithItemPageResponse();
        pageResponse.setId(dispatch.getId());
        pageResponse.setDispatchRef(dispatch.getDispatchRef());
        pageResponse.setDispatchInvoice(dispatch.getDispatchInvoice());
        pageResponse.setDispatchStatus(dispatch.getDispatchStatus());
        if (dispatch.getOrganization() != null) {
            Organization org=dispatch.getOrganization();
            OrganizationDto orgDto=new OrganizationDto();
            orgDto.setId(org.getId());
            orgDto.setName(org.getName());
            pageResponse.setOrganization(orgDto);
        }
        if (dispatch.getRequesterBranch() != null) {
            Branch requestedBranch=dispatch.getRequesterBranch();
            BranchDto requestedBranchDto=new BranchDto();
            requestedBranchDto.setId(requestedBranch.getId());
            requestedBranchDto.setName(requestedBranch.getName());
            pageResponse.setRequesterBranch(requestedBranchDto);
        }
        if (dispatch.getAcceptorBranch() != null) {
            Branch requestReceivedBranch=dispatch.getAcceptorBranch();
            BranchDto requestReceivedBranchDto=new BranchDto();
            requestReceivedBranchDto.setId(requestReceivedBranch.getId());
            requestReceivedBranchDto.setName(requestReceivedBranch.getName());
            pageResponse.setAcceptorBranch(requestReceivedBranchDto);
        }
        pageResponse.setTotalDispatchQty(dispatch.getTotalQty());
        pageResponse.setCreatedAt(dispatch.getCreatedAt());
        pageResponse.setTotalPurchasePrice(dispatch.getTotalPurchasePrice());
        //For Item Pagination
        pageResponse.setItems(page.getContent());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setPageNumber(page.getNumber());
        pageResponse.setPageSize(page.getSize());
        pageResponse.setTotalPages(page.getTotalPages());
        return pageResponse;
    }
}
