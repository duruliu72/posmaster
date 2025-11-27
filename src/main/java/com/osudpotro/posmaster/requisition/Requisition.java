package com.osudpotro.posmaster.requisition;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.requisitiontype.RequisitionType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Requisition extends BaseEntity {
    private Long targetEntityId;
    private String requisitionNo;
    @ManyToOne
    private RequisitionType requisitionType;
    @OneToMany(mappedBy = "requisition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequisitionApprover> approvers = new ArrayList<>();
}
