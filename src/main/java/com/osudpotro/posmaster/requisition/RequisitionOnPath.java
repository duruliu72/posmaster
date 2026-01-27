package com.osudpotro.posmaster.requisition;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "requisition_on_paths")
public class RequisitionOnPath extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "requisition_id")
    private Requisition requisition;
    private Integer reviewCount;
    @ManyToOne
    private User prevUser;
    @ManyToOne
    private User user;
    @ManyToOne
    private User nextUser;
    private Integer approvedStatus;//"1=Pending", "2=Approved", "3=rejected" "4=Review"
    private String comment;
}