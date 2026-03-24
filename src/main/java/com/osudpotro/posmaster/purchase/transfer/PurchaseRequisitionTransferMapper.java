package com.osudpotro.posmaster.purchase.transfer;

import com.osudpotro.posmaster.branch.BranchDto;
import com.osudpotro.posmaster.branch.BranchMapper;
import com.osudpotro.posmaster.organization.OrganizationDto;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisition;
import com.osudpotro.posmaster.tms.driver.DriverMapper;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsOnTrip;
import com.osudpotro.posmaster.tms.vechile.VehicleMapper;
import com.osudpotro.posmaster.tms.vehicletrip.VehicleTrip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PurchaseRequisitionTransferMapper {
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private VehicleMapper vehicleMapper;
    @Autowired
    private BranchMapper branchMapper;
    //Mapping Here
    //Entity → DTO
    public PurchaseRequisitionTransferDto toDto(PurchaseRequisitionTransfer prt) {
        if (prt == null) {
            return null;
        }
        PurchaseRequisitionTransferDto prtDto = new PurchaseRequisitionTransferDto();
        prtDto.setId(prt.getId());
        prtDto.setRequsitionRef(prt.getRequsitionRef());
        prtDto.setPurchaseInvoices(prt.getPurchaseInvoices());
        prtDto.setPurchaseInvoiceDocs(prt.getPurchaseInvoiceDocs());
        if (prt.getPurchaseRequisition() != null) {
            PurchaseRequisition pr = prt.getPurchaseRequisition();
            String purchaseCode = pr.getPurchaseType().getDescription();
            String purchaseKey = pr.getPurchaseType().getCode();
            prtDto.setPurchaseType(purchaseCode);
            prtDto.setPurchaseKey(purchaseKey);
            //        Organization
            OrganizationDto orgDto = new OrganizationDto();
            orgDto.setId(pr.getOrganization().getId());
            orgDto.setName(pr.getOrganization().getName());
            prtDto.setOrganization(orgDto);
            //        rootBranch
            BranchDto rootBranchDto = new BranchDto();
            rootBranchDto.setId(pr.getRootBranch().getId());
            rootBranchDto.setName(pr.getRootBranch().getName());
           // prtDto.setRootBranch(rootBranchDto);
            prtDto.setRootBranch(branchMapper.toDto(pr.getRootBranch()));
//        reqBranch
            BranchDto reqBranchDto = new BranchDto();
            reqBranchDto.setId(pr.getReqBranch().getId());
            reqBranchDto.setName(pr.getReqBranch().getName());
           // prtDto.setReqBranch(reqBranchDto);
            prtDto.setReqBranch(branchMapper.toDto(pr.getReqBranch()));
        }
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
        if (prt.getGoodsOnTrip() != null) {
            GoodsOnTrip goodsOnTrip = prt.getGoodsOnTrip();
            prtDto.setGoodsOnTripId(goodsOnTrip.getId());
            prtDto.setGoodsRef(goodsOnTrip.getGoodsRef());
            prtDto.setIsReceived(goodsOnTrip.getIsReceived());
            //Vehicle Trip Info
            if (goodsOnTrip.getVehicleTrip() != null) {
                VehicleTrip vehicleTrip = goodsOnTrip.getVehicleTrip();
                prtDto.setVehicleTripId(vehicleTrip.getId());
                prtDto.setTripRef(vehicleTrip.getTripRef());
                prtDto.setDriver(driverMapper.toDto(vehicleTrip.getDriver()));
                prtDto.setVehicle(vehicleMapper.toDto(vehicleTrip.getVehicle()));
            }
        }
        return prtDto;
    }

    public PurchaseRequisitionTransferWithItemPageResponse toMinDto(PurchaseRequisitionTransfer prt, Page<PurchaseRequisitionItemTransferDto> page) {
        PurchaseRequisitionTransferWithItemPageResponse pageResponse = new PurchaseRequisitionTransferWithItemPageResponse();
        pageResponse.setId(prt.getId());
        pageResponse.setRequsitionRef(prt.getRequsitionRef());
        pageResponse.setPurchaseInvoices(prt.getPurchaseInvoices());
        pageResponse.setPurchaseInvoiceDocs(prt.getPurchaseInvoiceDocs());
        if (prt.getPurchaseRequisition() != null) {
            PurchaseRequisition pr = prt.getPurchaseRequisition();
            String purchaseCode = pr.getPurchaseType().getDescription();
            String purchaseKey = pr.getPurchaseType().getCode();
            pageResponse.setPurchaseType(purchaseCode);
            pageResponse.setPurchaseKey(purchaseKey);
            //        Organization
            OrganizationDto orgDto = new OrganizationDto();
            orgDto.setId(pr.getOrganization().getId());
            orgDto.setName(pr.getOrganization().getName());
            pageResponse.setOrganization(orgDto);
            //        rootBranch
            BranchDto rootBranchDto = new BranchDto();
            rootBranchDto.setId(pr.getRootBranch().getId());
            rootBranchDto.setName(pr.getRootBranch().getName());
//            pageResponse.setRootBranch(rootBranchDto);
            pageResponse.setRootBranch(branchMapper.toDto(pr.getRootBranch()));
//        reqBranch
            BranchDto reqBranchDto = new BranchDto();
            reqBranchDto.setId(pr.getReqBranch().getId());
            reqBranchDto.setName(pr.getReqBranch().getName());
//            pageResponse.setReqBranch(reqBranchDto);
            pageResponse.setReqBranch(branchMapper.toDto(pr.getReqBranch()));
        }
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
        if (prt.getGoodsOnTrip() != null) {
            GoodsOnTrip goodsOnTrip = prt.getGoodsOnTrip();
            pageResponse.setGoodsOnTripId(goodsOnTrip.getId());
            pageResponse.setGoodsRef(goodsOnTrip.getGoodsRef());
            pageResponse.setIsReceived(goodsOnTrip.getIsReceived());
            if (goodsOnTrip.getVehicleTrip() != null) {
                VehicleTrip vehicleTrip = goodsOnTrip.getVehicleTrip();
                pageResponse.setVehicleTripId(vehicleTrip.getId());
                pageResponse.setTripRef(vehicleTrip.getTripRef());
                pageResponse.setDriver(driverMapper.toDto(vehicleTrip.getDriver()));
                pageResponse.setVehicle(vehicleMapper.toDto(vehicleTrip.getVehicle()));
            }
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
