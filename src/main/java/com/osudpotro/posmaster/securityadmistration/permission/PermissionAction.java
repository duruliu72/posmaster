package com.osudpotro.posmaster.securityadmistration.permission;

import com.osudpotro.posmaster.action.Action;
import com.osudpotro.posmaster.common.BaseEntity;
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
@Table(name = "permission_actions")
public class PermissionAction extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;
    @ManyToOne
    @JoinColumn(name = "action_id")
    private Action action;
    @JoinColumn(name = "is_active")
    private boolean isActive=false;
}