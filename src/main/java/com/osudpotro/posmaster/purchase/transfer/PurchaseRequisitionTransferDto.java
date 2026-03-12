package com.osudpotro.posmaster.purchase.transfer;

import com.osudpotro.posmaster.organization.OrganizationDto;
import com.osudpotro.posmaster.purchase.dto.BranchDto;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisition;
import com.osudpotro.posmaster.tms.driver.DriverDto;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsOnTripDto;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsStatus;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsType;
import com.osudpotro.posmaster.tms.vechile.VehicleDto;
import com.osudpotro.posmaster.tms.vehicletrip.TripStatus;
import com.osudpotro.posmaster.tms.vehicletrip.VehicleTripDto;
import com.osudpotro.posmaster.user.UserPlainDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseRequisitionTransferDto {
    private Long id;
    private PurchaseRequisition purchaseRequisition;
    private String requsitionRef;
    private String purchaseType;
    private String purchaseKey;
    private OrganizationDto organization;
    private BranchDto branch;
    private BigDecimal overallDiscount;
    private BigDecimal totalPrice;
    private Integer totalQty;
    private BigDecimal totalGiftOrBonusPrice;
    private Integer totalGiftOrBonusQty;
    private String purchaseInvoices;
    private String purchaseInvoiceDocs;
    private String orderRefs;
    private List<PurchaseRequisitionItemTransferDto> items = new ArrayList<>();
    private Integer transferStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    //    Goods On Trip Information
    private Long goodsOnTripId;
    private String goodsRef;
    private com.osudpotro.posmaster.branch.BranchDto sourceBranch;
    private String sourceAddress;
    private com.osudpotro.posmaster.branch.BranchDto destBranch;
    private String destAddress;
    private GoodsType goodsType;
    private String goodsReference;
    private String goodsReferenceDocs;
    private GoodsStatus goodsStatus;
    private UserPlainDto assignBy;
    private Boolean isReceived;
    private UserPlainDto receivedBy;
    private String signaturePath;
    private String remarks;
    private LocalDateTime loadedAt;
    private LocalDateTime unLoadedAt;
    private UserPlainDto loadedBy;
    private UserPlainDto unLoadedBy;
    //Vehicle Trip
    private Long vehicleTripId;
    private String tripRef;
    private DriverDto driver;
    private VehicleDto vehicle;
    private LocalDateTime tripStartTime;
    private LocalDateTime tripEndTime;
    private TripStatus tripStatus;
}
