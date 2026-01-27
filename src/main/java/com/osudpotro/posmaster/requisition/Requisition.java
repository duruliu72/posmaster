package com.osudpotro.posmaster.requisition;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisition;
import com.osudpotro.posmaster.requisitiontype.RequisitionType;
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
    private String note;
    public int getTotalPaths(){
        return requisitionOnPaths.size();
    }
}
