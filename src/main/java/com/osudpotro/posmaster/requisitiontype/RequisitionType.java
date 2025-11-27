package com.osudpotro.posmaster.requisitiontype;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.requisition.Approver;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class RequisitionType extends BaseEntity {
    // Example: "Purchase", "Local  Purchase", "Dispatch" etc.
    private String name;
    @Column(name = "requisition_type_key", nullable = false, unique = true, length = 50)
    private String requisitionTypeKey;
    @OneToMany(mappedBy = "requisitionType", cascade = CascadeType.ALL)
    private Set<Approver> approvers = new HashSet<>();
    public BigInteger getTotalApprover() {
        return BigInteger.valueOf(approvers.size());
    }
}
