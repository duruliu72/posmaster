package com.osudpotro.posmaster.tms.goodsontrip;

import com.osudpotro.posmaster.purchase.dto.BranchDto;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisition;
import com.osudpotro.posmaster.purchase.transfer.PurchaseRequisitionTransfer;
import com.osudpotro.posmaster.user.UserPlainDto;
import org.springframework.stereotype.Component;

@Component
public class GoodsOnTripMapper {
    public GoodsOnTripDto toDto(GoodsOnTrip goodsOnTrip) {
        if (goodsOnTrip == null) {
            return null;
        }
        GoodsOnTripDto goodsOnTripDto = new GoodsOnTripDto();
        goodsOnTripDto.setId(goodsOnTrip.getId());
        goodsOnTripDto.setGoodsRef(goodsOnTrip.getGoodsRef());
        goodsOnTripDto.setGoodsType(goodsOnTrip.getGoodsType());
        goodsOnTripDto.setGoodsReference(goodsOnTrip.getGoodsReference());
        goodsOnTripDto.setGoodsReferenceDocs(goodsOnTrip.getGoodsReferenceDocs());
        goodsOnTripDto.setGoodsStatus(goodsOnTrip.getGoodsStatus());
        goodsOnTripDto.setSignaturePath(goodsOnTrip.getSignaturePath());
        goodsOnTripDto.setRemarks(goodsOnTrip.getRemarks());
        goodsOnTripDto.setLoadedAt(goodsOnTrip.getLoadedAt());
        goodsOnTripDto.setUnLoadedAt(goodsOnTrip.getUnLoadedAt());
        goodsOnTripDto.setIsReceived(goodsOnTrip.getIsReceived());
        if (goodsOnTrip.getLoadedBy() != null) {
            var loadedBy = goodsOnTrip.getLoadedBy();
            UserPlainDto loadedByDto = new UserPlainDto();
            loadedByDto.setUserName(loadedBy.getUserName());
            loadedByDto.setId(loadedBy.getId());
            loadedByDto.setMobile(loadedBy.getMobile());
            loadedByDto.setEmail(loadedBy.getEmail());
            goodsOnTripDto.setLoadedBy(loadedByDto);
        }
        if (goodsOnTrip.getUnLoadedBy() != null) {
            var unLoadedBy = goodsOnTrip.getUnLoadedBy();
            UserPlainDto unLoadedByDto = new UserPlainDto();
            unLoadedByDto.setUserName(unLoadedBy.getUserName());
            unLoadedByDto.setId(unLoadedBy.getId());
            unLoadedByDto.setMobile(unLoadedBy.getMobile());
            unLoadedByDto.setEmail(unLoadedBy.getEmail());
            goodsOnTripDto.setLoadedBy(unLoadedByDto);
        }
        if (goodsOnTrip.getAssignBy() != null) {
            var assignBy = goodsOnTrip.getAssignBy();
            UserPlainDto assignByDto = new UserPlainDto();
            assignByDto.setUserName(assignBy.getUserName());
            assignByDto.setId(assignBy.getId());
            assignByDto.setMobile(assignBy.getMobile());
            assignByDto.setEmail(assignBy.getEmail());
            goodsOnTripDto.setAssignBy(assignByDto);
        }
        if (goodsOnTrip.getReceivedBy() != null) {
            var receivedBy = goodsOnTrip.getReceivedBy();
            UserPlainDto receivedByDto = new UserPlainDto();
            receivedByDto.setUserName(receivedBy.getUserName());
            receivedByDto.setId(receivedBy.getId());
            receivedByDto.setMobile(receivedBy.getMobile());
            receivedByDto.setEmail(receivedBy.getEmail());
            goodsOnTripDto.setReceivedBy(receivedByDto);
        }
        if (goodsOnTrip.getPurchaseRequisitionTransfer() != null) {
            PurchaseRequisitionTransfer prt = goodsOnTrip.getPurchaseRequisitionTransfer();
            if (prt.getPurchaseRequisition() != null) {
                PurchaseRequisition pr = prt.getPurchaseRequisition();
                //        rootBranch
                BranchDto rootBranchDto = new BranchDto();
                rootBranchDto.setId(pr.getRootBranch().getId());
                rootBranchDto.setName(pr.getRootBranch().getName());
                goodsOnTripDto.setSourceBranch(rootBranchDto);
//        reqBranch
                BranchDto reqBranchDto = new BranchDto();
                reqBranchDto.setId(pr.getReqBranch().getId());
                reqBranchDto.setName(pr.getReqBranch().getName());
                goodsOnTripDto.setDestBranch(reqBranchDto);
            }
        }
        return goodsOnTripDto;
    }
}
