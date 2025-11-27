package com.osudpotro.posmaster.requisition;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.requisitiontype.RequisitionType;
import com.osudpotro.posmaster.user.User;
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
public class Approver extends BaseEntity {
    @ManyToOne
    private User user ;
    @ManyToOne
    private User nextUser;
    @ManyToOne
    @JoinColumn(name = "requisition_type_id")
    private RequisitionType requisitionType;
}