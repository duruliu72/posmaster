package com.osudpotro.posmaster.purchase.transfer;

import com.osudpotro.posmaster.organization.OrganizationDto;
import com.osudpotro.posmaster.purchase.dto.BranchDto;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsOnTrip;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsOnTripMapper;
import com.osudpotro.posmaster.tms.vehicletrip.VehicleTrip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PurchaseRequisitionTransferMapper {
    @Autowired
    private GoodsOnTripMapper goodsOnTripMapper;
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
        prtDto.setCreatedAt(prt.getCreatedAt());
        prtDto.setTransferStatus(prt.getTransferStatus());
        //    Goods On Trip Information
        if(prt.getGoodsOnTrip()!=null){
            GoodsOnTrip goodsOnTrip=prt.getGoodsOnTrip();
            prtDto.setGoodsOnTripId(goodsOnTrip.getId());
            prtDto.setGoodsRef(goodsOnTrip.getGoodsRef());
            prtDto.setIsReceived(goodsOnTrip.getIsReceived());
            if(goodsOnTrip.getVehicleTrip()!=null){
                VehicleTrip vehicleTrip=goodsOnTrip.getVehicleTrip();
                prtDto.setVehicleTripId(vehicleTrip.getId());
                prtDto.setTripRef(vehicleTrip.getTripRef());
            }
            //Vehicle Trip
        }
        return prtDto;
    }

    public PurchaseRequisitionTransferWithItemPageResponse toMinDto(PurchaseRequisitionTransfer prt, Page<PurchaseRequisitionItemTransferDto> page) {
        PurchaseRequisitionTransferWithItemPageResponse pageResponse = new PurchaseRequisitionTransferWithItemPageResponse();
        pageResponse.setId(prt.getId());
        pageResponse.setRequsitionRef(prt.getRequsitionRef());
        String purchaseCode = prt.getPurchaseType().getDescription();
        String purchaseKey = prt.getPurchaseType().getCode();
        pageResponse.setPurchaseType(purchaseCode);
        pageResponse.setPurchaseKey(purchaseKey);
        OrganizationDto orgDto = new OrganizationDto();
        orgDto.setId(prt.getOrganization().getId());
        orgDto.setName(prt.getOrganization().getName());
        pageResponse.setOrganization(orgDto);
        BranchDto branchDto = new BranchDto();
        branchDto.setId(prt.getBranch().getId());
        branchDto.setName(prt.getBranch().getName());
        pageResponse.setBranch(branchDto);
//        pageResponse.setOverallDiscount(prt.getOverallDiscount());
//        pageResponse.setTotalPrice(prt.getTotalPrice());
//        pageResponse.setTotalQty(prt.getTotalQty());
//        pageResponse.setTotalGiftOrBonusQty(prt.getTotalGiftOrBonusQty());
//        pageResponse.setTotalGiftOrBonusPrice(prt.getTotalGiftOrBonusPrice());
        pageResponse.setPurchaseInvoices(prt.getPurchaseInvoices());
        pageResponse.setPurchaseInvoiceDocs(prt.getPurchaseInvoiceDocs());
        pageResponse.setOrderRefs(prt.getOrderRefs());
        pageResponse.setCreatedAt(prt.getCreatedAt());
        pageResponse.setTransferStatus(prt.getTransferStatus());
        //    Goods On Trip Information
        if(prt.getGoodsOnTrip()!=null){
            GoodsOnTrip goodsOnTrip=prt.getGoodsOnTrip();
            pageResponse.setGoodsOnTripId(goodsOnTrip.getId());
            pageResponse.setGoodsRef(goodsOnTrip.getGoodsRef());
            pageResponse.setIsReceived(goodsOnTrip.getIsReceived());
            if(goodsOnTrip.getVehicleTrip()!=null){
                VehicleTrip vehicleTrip=goodsOnTrip.getVehicleTrip();
                pageResponse.setVehicleTripId(vehicleTrip.getId());
                pageResponse.setTripRef(vehicleTrip.getTripRef());
            }
            //Vehicle Trip
        }
        //For Item Pagination
        pageResponse.setItems(page.getContent());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setPageNumber(page.getNumber());
        pageResponse.setPageSize(page.getSize());
        pageResponse.setTotalPages(page.getTotalPages());
        return pageResponse;
    }
}
