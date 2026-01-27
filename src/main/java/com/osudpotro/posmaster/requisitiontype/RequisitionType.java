package com.osudpotro.posmaster.requisitiontype;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.requisition.RequisitionApprover;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class RequisitionType extends BaseEntity {
    // Example: "Purchase", "Dispatch" etc.
    private String name;
    @Column(name = "requisition_type_key", nullable = false, unique = true, length = 50)
    private String requisitionTypeKey;
    @OneToMany(mappedBy = "requisitionType", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    private List<RequisitionApprover> requisitionApprovers = new ArrayList<>();
    public int getTotalApprover() {
        return requisitionApprovers.size();
    }
}
