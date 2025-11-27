package com.osudpotro.posmaster.security;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.resource.ApiResource;
import com.osudpotro.posmaster.role.Role;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "api_resource_id")
    private ApiResource apiResource;
    @JoinColumn(name = "is_resource_checked")
    private boolean isResourceChecked=false;
    @Enumerated(EnumType.STRING)
    @Column(name = "permission_type")
    private PermissionType permissionType; // ROLE or USER
    private boolean isEnable = true;
    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL)
    private Set<PermissionDetail> permissionDetails = new HashSet<>();

}