package com.osudpotro.posmaster.tms.goodsontrip;
import com.osudpotro.posmaster.branch.BranchDto;
import com.osudpotro.posmaster.tms.vehicletrip.VehicleTripDto;
import com.osudpotro.posmaster.user.UserPlainDto;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class GoodsOnTripDto{
    private Long id;
    private String goodsRef;
    private BranchDto sourceBranch;
    private String sourceAddress;
    private BranchDto destBranch;
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
    private VehicleTripDto vehicleTrip;
}
