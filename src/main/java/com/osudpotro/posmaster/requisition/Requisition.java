package com.osudpotro.posmaster.requisition;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisition;
import com.osudpotro.posmaster.requisitiontype.RequisitionType;
import com.osudpotro.posmaster.tms.goodsonvechile.GoodsOnTrip;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "requisitions")
public class Requisition extends BaseEntity {
    @OneToOne(mappedBy = "requisition", cascade = CascadeType.ALL)
    private PurchaseRequisition purchaseRequisition;
    private String requsitionRef;
    @ManyToOne
    private RequisitionType requisitionType;
    private Integer reviewCount;
    @OneToMany(mappedBy = "requisition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequisitionOnPath> requisitionOnPaths = new ArrayList<>();
    //    Like draft=1, Submitted=2,3=Approved,4=Rejected,5=Review,6=Cancelled,7=Closed(After all finally process done)
    private Integer requisitionStatus = 1;
    //1=Pending ,2=On the way,3=Arrived,4=Received
    private Integer deliveryStatus = 1;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "received_by")
    private User receivedBy;
    private String note;
    @ManyToOne
    private GoodsOnTrip goodsOnTrip;
    public boolean isPending() {
        return deliveryStatus==null||deliveryStatus == 1;
    }

    public boolean isOnTheWay() {
        return deliveryStatus == 2;
    }

    public boolean isArrived() {
        return deliveryStatus == 3;
    }

    public boolean isReceived() {
        return deliveryStatus == 4;
    }
    public String getDeliveryStatusText() {
        return switch (deliveryStatus) {
            case 1 -> "Pending";
            case 2 -> "On the way";
            case 3 -> "Arrived";
            case 4 -> "Received";
            default -> "Unknown";
        };
    }
    public int getTotalPaths() {
        return requisitionOnPaths.size();
    }
}
