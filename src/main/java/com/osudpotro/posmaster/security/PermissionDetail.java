package com.osudpotro.posmaster.security;

import com.osudpotro.posmaster.action.Action;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.resource.ApiResource;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permission_details")
public class PermissionDetail extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;
    @ManyToOne
    @JoinColumn(name = "action_id")
    private Action action;
    @JoinColumn(name = "is_action_checked")
    private boolean isActionChecked=false;
}