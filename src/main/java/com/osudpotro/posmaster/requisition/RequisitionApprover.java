package com.osudpotro.posmaster.requisition;

import com.osudpotro.posmaster.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class RequisitionApprover extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "requisition_id")
    private Requisition requisition;
    @ManyToOne
    @JoinColumn(name = "approver_id")
    private Approver approver;
    private int approvedStatus;//"1=Pending", "2=Approved", "3=rejected"
    private String comment;
}