package com.osudpotro.posmaster.requisition;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.requisitiontype.RequisitionType;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "requisition_approvers")
public class RequisitionApprover extends BaseEntity {
    @ManyToOne
    private User prevUser;
    @ManyToOne
    private User user ;
    @ManyToOne
    private User nextUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requisition_type_id")
    private RequisitionType requisitionType;
}